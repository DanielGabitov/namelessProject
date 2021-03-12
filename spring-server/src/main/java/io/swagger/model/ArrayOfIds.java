package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * ArrayOfIds
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2021-03-06T09:35:01.608Z")


public class ArrayOfIds   {
  @JsonProperty("posts")
  @Valid
  private List<Long> posts = null;

  public ArrayOfIds posts(List<Long> posts) {
    this.posts = posts;
    return this;
  }

  public ArrayOfIds addPostsItem(Long postsItem) {
    if (this.posts == null) {
      this.posts = new ArrayList<Long>();
    }
    this.posts.add(postsItem);
    return this;
  }

  /**
   * Get posts
   * @return posts
  **/
  @ApiModelProperty(value = "")


  public List<Long> getPosts() {
    return posts;
  }

  public void setPosts(List<Long> posts) {
    this.posts = posts;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ArrayOfIds arrayOfIds = (ArrayOfIds) o;
    return Objects.equals(this.posts, arrayOfIds.posts);
  }

  @Override
  public int hashCode() {
    return Objects.hash(posts);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ArrayOfIds {\n");
    
    sb.append("    posts: ").append(toIndentedString(posts)).append("\n");
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

