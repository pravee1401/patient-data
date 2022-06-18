package com.data.handler.patientdata.model;

import com.data.handler.patientdata.Gender;
import java.util.stream.Stream;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class GenderConverter implements AttributeConverter<Gender, String> {

  /**
   * Converts the value stored in the entity attribute into the data representation to be stored in the database.
   *
   * @param attribute the entity attribute value to be converted
   * @return the converted data to be stored in the database column
   */
  @Override
  public String convertToDatabaseColumn(Gender gender) {
    if (gender == null) {
      return null;
    }
    return gender.getGender();
  }

  /**
   * Converts the data stored in the database column into the value to be stored in the entity attribute. Note that it is the responsibility of the converter
   * writer to specify the correct <code>dbData</code> type for the corresponding column for use by the JDBC driver: i.e., persistence providers are not
   * expected to do such type conversion.
   *
   * @param dbData the data from the database column to be converted
   * @return the converted value to be stored in the entity attribute
   */
  @Override
  public Gender convertToEntityAttribute(String gender) {
    return Stream.of(Gender.values()).filter(genderEnum -> genderEnum.getGender().equalsIgnoreCase(gender)).findFirst()
        .orElseThrow(IllegalArgumentException::new);
  }
}
