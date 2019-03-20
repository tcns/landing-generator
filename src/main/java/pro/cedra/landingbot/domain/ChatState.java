package pro.cedra.landingbot.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;


/**
 * Created by tignatchenko on 23/04/17.
 */
@Entity
@Data
@ToString(exclude = {"chatUser"})
@EqualsAndHashCode(exclude = {"chatUser"})
public class ChatState implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Integer step;

    @Column
    private String data;

    @Column
    int previousMessageId;

    @JsonIgnore
    @OneToOne
    @PrimaryKeyJoinColumn
    private ChatUser chatUser;
}
