package group.itechart.orderplanning.repository.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.math.BigDecimal;

@Document
@Data
@AllArgsConstructor
public class WareHouseDocument {

    @Id
    private String id;

    private String name;

    private BigDecimal latitude;

    private BigDecimal longitude;


}
