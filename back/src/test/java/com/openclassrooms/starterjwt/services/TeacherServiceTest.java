package com.openclassrooms.starterjwt.services;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.services.TeacherService;

public class TeacherServiceTest {
	
	TeacherRepository teacherRepoMock = mock(TeacherRepository.class);
	
	@Test
	public void testFindAllTeachers() {
		List<Teacher> teacherList = new ArrayList<>();
		Teacher teacher = new Teacher();
		teacher.setId(1L);
		teacherList.add(teacher);
		when(teacherRepoMock.findAll()).thenReturn(teacherList);
		TeacherService teacherService = new TeacherService(teacherRepoMock);
		List<Teacher> foundTeachers = teacherService.findAll();

		Assertions.assertEquals(1, foundTeachers.size());
	}

	@Test
	public void testGetTeacherById() {
		Teacher teacher = new Teacher();
		teacher.setId(1L);
		when(teacherRepoMock.findById(anyLong())).thenReturn(Optional.of(teacher));
		TeacherService teacherService = new TeacherService(teacherRepoMock);
		Teacher foundTeacher = teacherService.findById(teacher.getId());

		Assertions.assertEquals(teacher.getId(), foundTeacher.getId());
	}

}
