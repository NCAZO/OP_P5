package controllers;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import org.springframework.http.HttpStatus;
import static org.junit.jupiter.api.Assertions.assertEquals;
import com.openclassrooms.starterjwt.controllers.TeacherController;
import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;
import java.util.ArrayList;
import java.util.List;

public class TeacherControllerTest {
	

    // Création de Mock
	TeacherMapper teacherMapperMock = mock(TeacherMapper.class);
	TeacherService teacherServiceMock = mock(TeacherService.class);
	
	@Test
	public void testTeacherFindByIdOK() {
		Long id = 1L;
		String name = "Alain";
		Teacher teacher = Teacher.builder().id(id).lastName("Terrieur").build();

		TeacherDto teacherDto = new TeacherDto();
		teacherDto.setId(id);
		teacherDto.setLastName(name);

		when(teacherServiceMock.findById(id)).thenReturn(teacher);
		when(teacherMapperMock.toDto(teacher)).thenReturn(teacherDto);
		TeacherController teacherController = new TeacherController(teacherServiceMock, teacherMapperMock);
		ResponseEntity<?> response = teacherController.findById(id.toString());

		verify(teacherServiceMock).findById(id);
		verify(teacherMapperMock).toDto(teacher);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(response.getBody(), teacherDto);
	}
	
	@Test
	public void testTeacherFindByIdNotOK() {
		Long id = 1L;

		when(teacherServiceMock.findById(id)).thenReturn(null);
		TeacherController teacherController = new TeacherController(teacherServiceMock, teacherMapperMock);
		ResponseEntity<?> response = teacherController.findById(id.toString());

		verify(teacherServiceMock).findById(id);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertEquals(response.getBody(), null);

	}
	
	@Test
	public void testTeacherFindByIdBadRequest() {

		TeacherController teacherController = new TeacherController(teacherServiceMock, teacherMapperMock);
		ResponseEntity<?> response = teacherController.findById("wrong ID");

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}
	
	@Test
	public void testTeacherFindAll() {
		List<Teacher> teachers = new ArrayList<>();
		teachers.add(new Teacher());
		teachers.add(new Teacher());
		teachers.add(new Teacher());

		List<TeacherDto> dtoTeacher = new ArrayList<>();
		dtoTeacher.add(new TeacherDto());
		dtoTeacher.add(new TeacherDto());
		dtoTeacher.add(new TeacherDto());

		when(teacherServiceMock.findAll()).thenReturn(teachers);
		when(teacherMapperMock.toDto(teachers)).thenReturn(dtoTeacher);
		TeacherController teacherController = new TeacherController(teacherServiceMock, teacherMapperMock);
		ResponseEntity<?> responseEntity = teacherController.findAll();

		assertEquals(200, responseEntity.getStatusCodeValue());
		assertEquals(dtoTeacher, responseEntity.getBody());
	}
}
