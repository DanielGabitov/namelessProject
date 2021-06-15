package com.hse.services;

import com.hse.DAOs.CreativeAssociationDao;
import com.hse.exceptions.ServiceException;
import com.hse.models.CreativeAssociation;
import com.hse.models.CreativeAssociationRegistrationData;
import com.hse.models.Event;
import com.hse.systems.FileSystemInteractor;
import com.hse.utils.Coder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CreativeAssociationService {

    private final CreativeAssociationDao associationDao;
    private final NotificationService notificationService;

    @Autowired
    public CreativeAssociationService(CreativeAssociationDao associationDao, NotificationService notificationService) {
        this.associationDao = associationDao;
        this.notificationService = notificationService;
    }

    @Transactional
    public void createCreativeAssociation(CreativeAssociationRegistrationData data){
        CreativeAssociation association = mapRegistrationData(data);
        long associationId  = associationDao.createCreativeAssociation(association);

        addMembers(associationId, association.getMembersIds());

        List<String> imageUUIDs = ImageService.saveImagesToFileSystem(association.getImages());
        addImages(associationId, imageUUIDs);
    }

    public void addImages(long associationId, List<String> imageUUIDs) {
        imageUUIDs.forEach(image -> associationDao.addImage(associationId, image));
    }

    public List<String> getImages(long associationId){

        return associationDao.getImages(associationId).stream()
                .map(FileSystemInteractor::getImage)
                .map(Coder::encode)
                .collect(Collectors.toList());
    }

    public List<Long> getMembers(long associationId){
        return associationDao.getMembers(associationId);
    }

    public void addMembers(long associationId, List<Long> participants) {
        participants.forEach(member -> associationDao.addMember(associationId, member));
    }

    private CreativeAssociation mapRegistrationData(CreativeAssociationRegistrationData data) {
        CreativeAssociation association = new CreativeAssociation();
        association.setName(data.getName());
        association.setDescription(data.getDescription());
        association.setBossCreator(data.getBossCreator());
        association.setImages(data.getImages());
        association.setMembersIds(List.of(association.getBossCreator()));

        return association;
    }

    public CreativeAssociation getCreativeAssociation(long associationId){
        var association = getCreativeAssociationFromMainTable(associationId);
        return setAssociationDataFromOtherTables(association);
    }

    private CreativeAssociation getCreativeAssociationFromMainTable(long associationId){
        var optionalCreativeAssociation = associationDao.getCreativeAssociation(associationId);
        if (optionalCreativeAssociation.isEmpty()){
            String errorMessage = "There is no creative association with id " + associationId;
            throw new ServiceException(HttpStatus.BAD_REQUEST, errorMessage);
        }
        return optionalCreativeAssociation.get();
    }

    private CreativeAssociation setAssociationDataFromOtherTables(CreativeAssociation association) {
        Long id = association.getId();
        association.setImages(getImages(id));
        association.setMembersIds(getMembers(id));
        return association;
    }

    @Transactional
    public void inviteCreator(long associationId, long creatorId){
        var optionalInvitation = associationDao.getInvitation(associationId, creatorId);
        if (optionalInvitation.isPresent()){
            String errorMessage = "Creative association " + associationId + " has already invited creator " + creatorId;
            throw new ServiceException(HttpStatus.BAD_REQUEST, errorMessage);
        }
        associationDao.addInvitation(associationId, creatorId);
        var association = getCreativeAssociationFromMainTable(associationId);
        notificationService.sendCreativeAssociationInvitationNotification(association.getBossCreator(), creatorId);
    }

    //todo в других методов с нотификациями обработка исключений
    @Transactional
    public void answerCreativeAssociationInvitation(long associationId, long creatorId, boolean accepted){
        var optionalAssociationInvitation
                = associationDao.getInvitation(associationId, creatorId);
        if (optionalAssociationInvitation.isEmpty()){
            String errorMessage = "Creator " + creatorId
                    + " has no invitation to creative association " + associationId;
            throw new ServiceException(HttpStatus.BAD_REQUEST, errorMessage);
        }
        var invitation = optionalAssociationInvitation.get();
        if (invitation.getIsAnswered()){
            String errorMessage = "Creator " + creatorId + "has already ";
            errorMessage += ((invitation.getAccepted()) ? "accepted" : "rejected");
            errorMessage += "invitation to creative association" + associationId;
            throw new ServiceException(HttpStatus.BAD_REQUEST, errorMessage);
        }
        associationDao.answerInvitation(associationId, creatorId, accepted);
        var association = getCreativeAssociationFromMainTable(associationId);
        notificationService.sendCreativeAssociationInvitationAnswerNotification(association.getBossCreator(), creatorId, accepted);
        if (accepted){
            associationDao.addMember(associationId, creatorId);
        }
    }

    public List<CreativeAssociation> getCreativeAssociations(int size, int offset){
        return associationDao.getCreativeAssociations(size, offset).stream()
                .map(this::setAssociationDataFromOtherTables)
                .collect(Collectors.toList());
    }
}
