package com.springbootbatch.step;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import com.springbootbatch.entities.Person;

public class PersonCsvItemReader extends FlatFileItemReader<Person> {

    @Autowired
    private ResourceLoader resourceLoader;
    private String filename;

    /**
     * Sets the filename of the CSV file to be read.
     * 
     * @param filename the name of the CSV file
     * @throws IllegalArgumentException if the file cannot be opened
     */
    public void setFilename(String filename) {
        this.filename = filename;
        Resource resource = new ClassPathResource(filename);
        try {
            resource.getInputStream().close(); // Intenta abrir y cerrar el recurso para verificar si existe
        } catch (IOException e) {
            throw new IllegalArgumentException("No se puede abrir el archivo: " + filename, e);
        }
        setResource(resource);
    }

    private static final LineMapper<Person> lineMapper = createLineMapper();

    public PersonCsvItemReader() {
        setName("personReader");
        setLinesToSkip(1);
        setLineMapper(lineMapper);
        setEncoding(StandardCharsets.UTF_8.name());
    }

    private static LineMapper<Person> createLineMapper() {
        DefaultLineMapper<Person> lineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();

        tokenizer.setNames("cardId", "name", "lastName", "age");
        tokenizer.setIncludedFields(0, 1, 2, 3);
        tokenizer.setDelimiter(",");

        BeanWrapperFieldSetMapper<Person> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Person.class);

        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);

        return lineMapper;
    }
}
