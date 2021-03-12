swagger: "2.0"
info:
  version: "1.0.0"
  title: "Swagger Petstore"
host: "localhost:8080"
schemes:
- "https"
- "http"
paths:
  /users:
    post:
      tags:
      - "users"
      description: "New user has registrated."
      operationId: "createUser"
      consumes:
      - "application/json"
      produces:
      - "application/json"
      parameters:
      - in: "body"
        name: "regData"
        description: "User data"
        required: true
        schema:
          $ref: "#/definitions/registrationData"      
      responses:
        "405":
          description: "Invalid input"
        "404":
          description: "Some error idk"
        "200":
          description: "Successful operation"
 
 
  /users/{userId}:
    get:
      tags:
      - "users"
      description: "Get all information about user with given userId."
      operationId: "getUserData"
      produces:
      - "application/json"
      parameters:
      - name: "userId"
        in: "path"
        description: "ID of a user"
        required: true
        type: "integer"
        format: "int64"
      responses:
        "200":
          description: "successful operation"
          schema:
            $ref: "#/definitions/userData"
        "404":
          description: "User not found"
 
 
    delete:
      tags:
      - "users"
      description: "Delete user"
      operationId: "deleteUser"
      parameters:
      - name: "userId"
        in: "path"
        description: "ID of a user"
        required: true
        type: "integer"
        format: "int64"
      responses:
        "200":
          description: "successful operation"
        "404":
          description: "User not found"
 
 
  /users/{userId}/posts:
    get:
      tags:
      - "users"
      description: "Get all user's posts."
      operationId: "getUserPosts"
      produces:
      - "application/json"
      parameters:
      - name: "userId"
        in: "path"
        description: "ID of a user"
        required: true
        type: "integer"
        format: "int64"
      responses:
        "200":
          description: "successful operation"
          schema:
            $ref: "#/definitions/arrayOfIds"
        "404":
          description: "User not found"
 
 
  /users/{userId}/events:
    get:
      tags:
      - "users"
      description: "Get all user's events."
      operationId: "getUserEvents"
      produces:
      - "application/json"
      parameters:
      - name: "userId"
        in: "path"
        description: "ID of a user"
        required: true
        type: "integer"
        format: "int64"
      responses:
        "200":
          description: "successful operation"
          schema:
            $ref: "#/definitions/arrayOfIds"
        "404":
          description: "User not found"
 
 
  /events:
    post:
      tags:
      - "events"
      operationId: "createEvents"
      consumes:
      - "application/json"
      produces:
      - "application/json"
      parameters:
      - in: "body"
        name: "body"
        required: true
        schema:
          $ref: "#/definitions/event"
      responses:
        "200":
          description: "successful operation"
 
    get:
      tags:
      - "events"
      operationId: "Get all events"
      produces:
      - "application/json"
      responses:
        "200":
          description: "successful operation"
          schema:
            $ref: "#/definitions/arrayOfIds"
 
  /events/{eventId}:
    get:
      tags:
      - "events"
      operationId: "getEventById"
      produces:
      - "application/json"
      parameters:
      - name: "eventId"
        in: "path"
        description: ""
        required: true
        type: "integer"
      responses:
        "200":
          description: "successful operation"
          schema:
            $ref: "#/definitions/event"
        "400":
          description: "Invalid id"
        "404":
          description: "events not found"
 
    put:
      tags:
      - "events"
      operationId: "updatEvent"
      consumes:
      - "application/json"
      parameters:
      - name: "eventId"
        in: "path"
        required: true
        type: "integer"
        format: "int64"
      - in: "body"
        name: "body"
        required: true
        schema:
          $ref: "#/definitions/event"
      responses:
        "200":
          description: "successful operation"
        "404":
          description: "events not found"
 
    delete:
      tags:
      - "events"
      operationId: "deleteEvent"
      parameters:
      - name: "eventId"
        in: "path"
        required: true
        type: "integer"
        format: "int64"
      responses:
        "400":
          description: "Invalid ID supplied"
        "404":
          description: "events not found"
 
 
  /posts:
    post:
      tags:
      - "posts"
      operationId: "createPost"
      consumes:
      - "application/json"
      produces:
      - "application/json"
      parameters:
      - in: "body"
        name: "body"
        required: true
        schema:
          $ref: "#/definitions/post"
      responses:
        "200":
          description: "successful operation"
 
    get:
      tags:
      - "posts"
      operationId: "GetAllPosts"
      produces:
      - "application/json"
      responses:
        "200":
          description: "Successful operation"
          schema:
            $ref: "#/definitions/arrayOfIds"
 
 
  /posts/{id}:
    get:
      tags:
      - "posts"
      operationId: "getPostById"
      produces:
      - "application/json"
      parameters:
      - name: "id"
        in: "path"
        description: ""
        required: true
        type: "integer"
      responses:
        "200":
          description: "successful operation"
          schema:
            $ref: "#/definitions/post"
        "400":
          description: "Invalid id"
        "404":
          description: "events not found"
 
    put:
      tags:
      - "posts"
      operationId: "updatePosts"
      consumes:
      - "application/json"
      parameters:
      - name: "id"
        in: "path"
        required: true
        type: "integer"
        format: "int64"
      - in: "body"
        name: "body"
        required: true
        schema:
          $ref: "#/definitions/post"
      responses:
        "404":
          description: "Event not found"
 
    delete:
      tags:
      - "posts"
      operationId: "deletePost"
      parameters:
      - name: "id"
        in: "path"
        required: true
        type: "integer"
        format: "int64"
      responses:
        "400":
          description: "Invalid ID supplied"
        "404":
          description: "posts not found"
 
 
  /likes:
    post:
      tags:
      - "likes"
      description: "New like has registrated."
      operationId: "createLike"
      consumes:
      - "application/json"
      produces:
      - "application/json"
      parameters:
      - in: "body"
        name: "likeData"
        description: "Like data"
        required: true
        schema:
          $ref: "#/definitions/like"      
      responses:
        "200":
          description: "Successful operation"
        "404":
          description: "Some error idk"
        "405":
          description: "Invalid input"
 
 
  /likes/{likeId}:
    get:
      tags:
      - "likes"
      description: "Get all information about like with given likeId."
      operationId: "getLikeData"
      produces:
      - "application/json"
      parameters:
      - name: "likeId"
        in: "path"
        description: "ID of a like"
        required: true
        type: "integer"
        format: "int64"
      responses:
        "200":
          description: "successful operation"
          schema:
            $ref: "#/definitions/like"
        "404":
          description: "Like not found"
 
    delete:
      tags:
      - "likes"
      description: "Delete like"
      operationId: "deleteLike"
      parameters:
      - name: "likeId"
        in: "path"
        description: "ID of a like"
        required: true
        type: "integer"
        format: "int64"
      responses:
        "200":
          description: "successful operation"
        "404":
          description: "Like not found"


  /attendances:
    post:
      tags:
      - "attendances"
      description: "New attendance."
      operationId: "createAttendance"
      consumes:
      - "application/json"
      produces:
      - "application/json"
      parameters:
      - in: "body"
        name: "attendanceData"
        description: "attendance data"
        required: true
        schema:
          $ref: "#/definitions/attendance"      
      responses:
        "200":
          description: "Successful operation"
        "404":
          description: "Some error idk"
        "405":
          description: "Invalid input"
 
 
  /attendances/{attendanceId}:
    get:
      tags:
      - "attendances"
      description: "Get all information about attendance with given attendanceId."
      operationId: "getAttendanceData"
      produces:
      - "application/json"
      parameters:
      - name: "attendanceId"
        in: "path"
        description: "ID of a attendance"
        required: true
        type: "integer"
        format: "int64"
      responses:
        "200":
          description: "successful operation"
          schema:
            $ref: "#/definitions/attendance"
        "404":
          description: "Attendance not found"
 
    delete:
      tags:
      - "attendances"
      description: "Delete attendance"
      operationId: "deleteAttendance"
      parameters:
      - name: "attendanceId"
        in: "path"
        description: "ID of a attendance"
        required: true
        type: "integer"
        format: "int64"
      responses:
        "200":
          description: "successful operation"
        "404":
          description: "Like not found"

  /profiles:
    post:
      tags:
      - "profiles"
      description: "New profile."
      operationId: "createProfile"
      consumes:
      - "application/json"
      produces:
      - "application/json"
      parameters:
      - in: "body"
        name: "profileData"
        description: "profile data"
        required: true
        schema:
          $ref: "#/definitions/profile"      
      responses:
        "200":
          description: "Successful operation"
        "404":
          description: "Some error idk"
        "405":
          description: "Invalid input"
 
 
  /profiles/{profileId}:
    get:
      tags:
      - "attendances"
      description: "Get all information about profile with given profileId."
      operationId: "getProfileData"
      produces:
      - "application/json"
      parameters:
      - name: "profileId"
        in: "path"
        description: "ID of a profile"
        required: true
        type: "integer"
        format: "int64"
      responses:
        "200":
          description: "successful operation"
          schema:
            $ref: "#/definitions/profile"
        "404":
          description: "Profile not found"
 
    delete:
      tags:
      - "profiles"
      description: "Delete profile"
      operationId: "deleteProfile"
      parameters:
      - name: "profileId"
        in: "path"
        description: "ID of a profile"
        required: true
        type: "integer"
        format: "int64"
      responses:
        "200":
          description: "successful operation"
        "404":
          description: "Profile not found"
 
definitions:

 
  event:
    type: "object"
    properties:
      id:
        type: "integer"
      description:
        type: "string"
      userId:
        type: "integer"
        format: "int64"        
 
 
  post:
    type: "object"
    properties:
      id:
        type: "integer"
      description:
        type: "string"
      userId:
        type: "integer"
        format: "int64"
 
 
  registrationData:
    type: "object"
    properties:
      id:
        type: "integer"
        format: "int64"
      UserName:
        type: "string"
 
 
  arrayOfIds:
    type: "object"
    properties:
      posts:
        type: "array"
        items:
          type: "integer"
          format: "int64"
 
  userData:
    type: "object"
    properties:
      id:
        type: "integer"
        format: "int64"
      userType:
        type: "string"
        enum:
        - "User"
        - "Content Maker"
        - "Host"
      userName:
        type: "string"
      fullUserName:
        type: "string"
 
 
  like:
    type: "object"
    properties:
      likeId:
        type: "integer"
        format: "int64"
      user_id:
        type: "integer"
        format: "int64"
      activityId:
        type: "integer"
        format: "int64"
      activityType:
        type: "string"
        enum:
        - "Event"
        - "Post"
      date:
        type: "string"
 
 
  attendance:
    type: "object"
    properties:
      user_id:
        type: "integer"
        format: "int64"
      event_id:
        type: "integer"
        format: "int64"
 
 
  profile:
    type: "object"
    properties:
      id:
        type: "integer"
        format: "int64"
      text:
        type: "string"
      createDate:
        type: "string"
      author:
        type: "string"