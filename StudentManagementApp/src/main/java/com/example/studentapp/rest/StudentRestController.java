package com.example.studentapp.rest;

import com.example.studentapp.dao.StudentDAO;
import com.example.studentapp.model.Student;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/students")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class StudentRestController {

    private final StudentDAO dao = new StudentDAO();

    @GET
    public Response all(@QueryParam("page") @DefaultValue("1") int page,
                        @QueryParam("size") @DefaultValue("10") int size,
                        @QueryParam("search") String search) {
        List<Student> students = dao.getAll(page, size, search);
        return Response.ok(students).build();
    }

    @GET
    @Path("/{id}")
    public Response one(@PathParam("id") int id) {
        Student s = dao.find(id);
        if (s == null) return Response.status(Response.Status.NOT_FOUND).entity("{\"message\":\"Student not found\"}").build();
        return Response.ok(s).build();
    }

    @POST
    public Response create(Student student) {
        if (student == null || student.getName() == null || student.getEmail() == null || student.getCourse() == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("{\"message\":\"Invalid payload\"}").build();
        }
        if (dao.findByEmail(student.getEmail()) != null) {
            return Response.status(Response.Status.CONFLICT).entity("{\"message\":\"Email exists\"}").build();
        }
        dao.save(student);
        return Response.status(Response.Status.CREATED).entity("{\"message\":\"Student created\"}").build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") int id, Student student) {
        Student existing = dao.find(id);
        if (existing == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("{\"message\":\"Student not found\"}").build();
        }
        existing.setName(student.getName());
        existing.setEmail(student.getEmail());
        existing.setCourse(student.getCourse());
        dao.update(existing);
        return Response.ok("{\"message\":\"Student updated\"}").build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") int id) {
        Student existing = dao.find(id);
        if (existing == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("{\"message\":\"Student not found\"}").build();
        }
        dao.delete(id);
        return Response.ok("{\"message\":\"Student deleted\"}").build();
    }
}