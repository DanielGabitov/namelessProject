package com.hse.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import java.util.Objects;

/**
 * UserData
 */
@Validated



public class UserData   {
  @JsonProperty("id")
  private Long id;

  public UserData(Long id, UserTypeEnum userType, String userName, String fullUserName) {
    this.id = id;
    this.userType = userType;
    this.userName = userName;
    this.fullUserName = fullUserName;
  }

  /**
   * Gets or Sets userType
   */
  public enum UserTypeEnum {
    USER("User"),
    
    CONTENT_MAKER("Content Maker"),
    
    HOST("Host");

    private String value;

    private UserTypeEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static UserTypeEnum fromValue(String text) {
      for (UserTypeEnum b : UserTypeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("userType")
  private UserTypeEnum userType = null;

  @JsonProperty("userName")
  private String userName = null;

  @JsonProperty("fullUserName")
  private String fullUserName = null;

  public UserData id(Long id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
  **/
  @ApiModelProperty(value = "")


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public UserData userType(UserTypeEnum userType) {
    this.userType = userType;
    return this;
  }

  /**
   * Get userType
   * @return userType
  **/
  @ApiModelProperty(value = "")


  public UserTypeEnum getUserType() {
    return userType;
  }

  public void setUserType(UserTypeEnum userType) {
    this.userType = userType;
  }

  public UserData userName(String userName) {
    this.userName = userName;
    return this;
  }

  /**
   * Get userName
   * @return userName
  **/
  @ApiModelProperty(value = "")


  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public UserData fullUserName(String fullUserName) {
    this.fullUserName = fullUserName;
    return this;
  }

  /**
   * Get fullUserName
   * @return fullUserName
  **/
  @ApiModelProperty(value = "")


  public String getFullUserName() {
    return fullUserName;
  }

  public void setFullUserName(String fullUserName) {
    this.fullUserName = fullUserName;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserData userData = (UserData) o;
    return Objects.equals(this.id, userData.id) &&
        Objects.equals(this.userType, userData.userType) &&
        Objects.equals(this.userName, userData.userName) &&
        Objects.equals(this.fullUserName, userData.fullUserName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, userType, userName, fullUserName);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserData {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    userType: ").append(toIndentedString(userType)).append("\n");
    sb.append("    userName: ").append(toIndentedString(userName)).append("\n");
    sb.append("    fullUserName: ").append(toIndentedString(fullUserName)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

