/**
 * NOTE: This class is auto generated by the swagger code generator program (2.4.19).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package com.hse.controller;

import com.hse.model.ArrayOfIds;
import com.hse.model.Post;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;


@Validated
@Api(value = "posts", description = "the posts API")
@RequestMapping(value = "/api")
public interface PostsApi {

    @ApiOperation(value = "", nickname = "createPost", notes = "", tags={ "posts", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation") })
    @RequestMapping(value = "/posts",
        consumes = { "application/json" },
        method = RequestMethod.POST)
    ResponseEntity<Void> createPost(@ApiParam(value = "" ,required=true )  @Valid @RequestBody Post body);


    @ApiOperation(value = "", nickname = "deletePost", notes = "", tags={ "posts", })
    @ApiResponses(value = { 
        @ApiResponse(code = 400, message = "Invalid ID supplied"),
        @ApiResponse(code = 404, message = "posts not found") })
    @RequestMapping(value = "/posts/{id}",
        method = RequestMethod.DELETE)
    ResponseEntity<Void> deletePost(@ApiParam(value = "",required=true) @PathVariable("id") Long id);


    @ApiOperation(value = "", nickname = "getAllPosts", notes = "", response = ArrayOfIds.class, tags={ "posts", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful operation", response = ArrayOfIds.class) })
    @RequestMapping(value = "/posts",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<ArrayOfIds> getAllPosts();


    @ApiOperation(value = "", nickname = "getPostById", notes = "", response = Post.class, tags={ "posts", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation", response = Post.class),
        @ApiResponse(code = 400, message = "Invalid id"),
        @ApiResponse(code = 404, message = "events not found") })
    @RequestMapping(value = "/posts/{id}",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<Post> getPostById(@ApiParam(value = "",required=true) @PathVariable("id") Integer id);


    @ApiOperation(value = "", nickname = "updatePosts", notes = "", tags={ "posts", })
    @ApiResponses(value = { 
        @ApiResponse(code = 404, message = "Event not found") })
    @RequestMapping(value = "/posts/{id}",
        consumes = { "application/json" },
        method = RequestMethod.PUT)
    ResponseEntity<Void> updatePosts(@ApiParam(value = "",required=true) @PathVariable("id") Long id,@ApiParam(value = "" ,required=true )  @Valid @RequestBody Post body);

}