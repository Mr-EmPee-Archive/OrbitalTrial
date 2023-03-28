package ml.empee.orbitaltrial.model.dto;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DatabaseTable(tableName = "accounts")
public class AccountDTO {

  @DatabaseField(id = true)
  private String uuid;
  @DatabaseField
  private Double balance;

}
