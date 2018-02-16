package de.tutorial.jaxrs.api.chariot.rest.resource;

import io.swagger.annotations.*;


import javax.ws.rs.core.Response;

import com.gtarc.servicedirectory.api.chariot.rest.data.UserData;

import de.tutorial.jaxrs.model.runtimeenvironment.User;

import javax.ws.rs.*;

@Path("/user")
@Api(value="/user", description = "Operations about user")
@Produces({"application/json"})
public class UserResource {
  static UserData userData = new UserData();

  @POST
  @ApiOperation(value = "Create user", 
    notes = "This can only be done by the logged in user.")
  public Response createUser(
      @ApiParam(value = "Created user object", required = true) de.tutorial.jaxrs.model.runtimeenvironment.User user) {
    userData.addUser(user);
    return Response.ok().entity("").build();
  }

    @POST
    @Path("/createWithArray")
    @ApiOperation(value = "Creates list of users with given input array")
    public Response createUsersWithArrayInput(@ApiParam(value = "List of user object", required = true) User[] users) {
        for (de.tutorial.jaxrs.model.runtimeenvironment.User user : users) {
            userData.addUser(user);
        }
        return Response.ok().entity("").build();
    }

    @POST
    @Path("/createWithList")
    @ApiOperation(value = "Creates list of users with given input array")
    public Response createUsersWithListInput(@ApiParam(value = "List of user object", required = true) java.util.List<User> users) {
        for (User user : users) {
            userData.addUser(user);
        }
        return Response.ok().entity("").build();
    }

  @PUT
  @Path("/{username}")
  @ApiOperation(value = "Updated user", 
    notes = "This can only be done by the logged in user.")
  @ApiResponses(value = {
      @ApiResponse(code = 400, message = "Invalid user supplied"),
      @ApiResponse(code = 404, message = "User not found") })
  public Response updateUser(
      @ApiParam(value = "name that need to be deleted", required = true) @PathParam("username") String username,
      @ApiParam(value = "Updated user object", required = true) User user) {
    userData.addUser(user);
    return Response.ok().entity("").build();
  }

  @DELETE
  @Path("/{username}")
  @ApiOperation(value = "Delete user", 
    notes = "This can only be done by the logged in user.")
  @ApiResponses(value = {
      @ApiResponse(code = 400, message = "Invalid username supplied"),
      @ApiResponse(code = 404, message = "User not found") })
  public Response deleteUser(
      @ApiParam(value = "The name that needs to be deleted", required = true) @PathParam("username") String username) {
    if (userData.removeUser(username)) {
      return Response.ok().entity("").build();
    } else {
      return Response.status(Response.Status.NOT_FOUND).build();
    }
  }

  @GET
  @Path("/{username}")
  public Response getUserByName(String username) {
    User user = userData.findUserByName(username);
    if (null != user) {
      return Response.ok().entity(user).build();
    } else {
//      throw new NotFoundException(404, "User not found");
    }
	return null;
  }

  @GET
  @Path("/login")
  @ApiOperation(value = "Logs user into the system", 
    response = String.class)
  @ApiResponses(value = { @ApiResponse(code = 400, message = "Invalid username/password supplied") })
  public Response loginUser(
      @ApiParam(value = "The user name for login", required = true) @QueryParam("username") String username,
      @ApiParam(value = "The password for login in clear text", required = true) @QueryParam("password") String password) {
    return Response.ok()
        .entity("logged in user session:" + System.currentTimeMillis())
        .build();
  }

  @GET
  @Path("/logout")
  @ApiOperation(value = "Logs out current logged in user session")
  public Response logoutUser() {
    return Response.ok().entity("").build();
  }
}