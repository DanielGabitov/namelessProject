package com.hse.controllers;


import com.hse.models.CreativeAssociation;
import com.hse.models.CreativeAssociationRegistrationData;
import com.hse.services.CreativeAssociationService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/creativeAssociation")
public class CreativeAssociationController {

    private final CreativeAssociationService associationService;

    @Autowired
    public CreativeAssociationController(CreativeAssociationService associationService) {
        this.associationService = associationService;
    }

    @PostMapping(consumes = {"application/json"})
    @ApiOperation(value = "", nickname = "Create new creative association.", tags = {"CA"})
    public ResponseEntity<Void> createCreativeAssociation(@RequestBody CreativeAssociationRegistrationData data) {
        associationService.createCreativeAssociation(data);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/{associationId}")
    @ApiOperation(value = "", nickname = "Get creative association.", tags = {"CA"})
    public ResponseEntity<CreativeAssociation> getCreativeAssociation(@PathVariable("associationId") long associationId) {
        return new ResponseEntity<>(associationService.getCreativeAssociation(associationId), HttpStatus.OK);
    }

    @PostMapping(value = "/{associationId}/members/{creatorToInviteId}")
    @ApiOperation(value = "", nickname = "Invite creator to creative association", tags = {"CA"})
    public ResponseEntity<Void> inviteCreator(@PathVariable("associationId") long associationId,
                                              @PathVariable("creatorToInviteId") long creatorId) {
        associationService.inviteCreator(associationId, creatorId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
