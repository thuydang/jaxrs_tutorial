package de.tutorial.jaxrs.api.chariot.rest.resource;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.net.URI;
import java.util.UUID;

import javax.ws.rs.*;

import io.swagger.annotations.*;
import de.tutorial.jaxrs.api.chariot.rest.data.DummyServiceDirectory;
import de.tutorial.jaxrs.model.runtimeenvironment.Device;
import de.tutorial.jaxrs.model.runtimeenvironment.SensingDevice;

/**
 * 
 * @author dang
 *
 */
@Path("/device")
@Api(value="/device", description = "Operations about device")
@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, "application/jsonchariot" })
@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, "application/jsonchariot" })
public class DeviceResource {

	/** TODO: Logging? */
	/** TODO: Inject biz logic for handling RE, agent bean and so? */
	DummyServiceDirectory serviceDirectoryConnector = DummyServiceDirectory.createServiceDirectoryConnector();

	/**
	 * Create description of the runtime environment in Service Directory.
	 * TODO: notify respective Agent?
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Create / Register runtime environment", 
	notes = "Create description of the runtime environment in Service Directory.")
	public Response register(
			@ApiParam(value = "Created RE description", required = true) Device device) {
		serviceDirectoryConnector.addDevice(device);
		return Response.ok().entity("").build();
	}

	@POST
	@Path("/echoRegister")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, "application/jsonchariot" })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, "application/jsonchariot" })
	@ApiOperation(value = "Echo service", 
	notes = "Create description of the runtime environment in Service Directory.")
	public Response echoRegister(
			@ApiParam(value = "entitiy parameter", required = true) Device device) {
		//serviceDirectoryConnector.addDevice(device);
		//System.out.println(device);
		return Response.ok().entity(device).build();
	}

	
	/**
	 * Delete description of the runtime environment in Service Directory.
	 * TODO: notify respective Agent?
	 */
	@DELETE
	@Path("/{id}")
	@ApiOperation(value = "Delete user", 
	notes = "This can only be done by the logged in user.")
	@ApiResponses(value = {
			@ApiResponse(code = 400, message = "Invalid username supplied"),
			@ApiResponse(code = 404, message = "User not found") })
	public Response unRegister(
			@ApiParam(value = "The id of the RE that needs to be deleted", required = true) @PathParam("id") URI id) {
		if (serviceDirectoryConnector.removeDevice(id)) {
			return Response.ok().entity("").build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}

}
