package br.com.caelum.leilao.dao;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class LocalDatePersistenceConverter implements AttributeConverter<LocalDate, Date> {

	@Override
	public Date convertToDatabaseColumn(LocalDate attribute) {
		if (attribute != null) {
			return Date.from(((LocalDate) attribute).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
		}
		return null;
	}

	@Override
	public LocalDate convertToEntityAttribute(Date dbData) {
		if (dbData != null) {
			return Instant.ofEpochMilli(((Date) dbData).getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
		}
		return null;
	}

}
