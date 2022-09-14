package com.example.smodytestdbbatch;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataBatch {

	private final EntityManager em;

	@Transactional
	@PostConstruct
	public void setUp() {

	}

}
