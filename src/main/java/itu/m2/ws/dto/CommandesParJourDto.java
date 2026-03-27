package itu.m2.ws.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommandesParJourDto {
    private Date date;
    private Long nombreCommandes;
}
