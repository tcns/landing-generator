package pro.cedra.landingbot.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


/**
 * Created by tignatchenko on 25/04/17.
 */
@Entity
@Data
@EqualsAndHashCode(exclude={"yaToken", "telegramUsername", "landings", "chatState"})
@ToString(exclude={"yaToken", "telegramUsername", "landings", "chatState"})
public class ChatUser implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String telegramUsername;

    @Column
    private Long telegramChatId;

    @Column
    private String yaToken;

    @OneToOne(fetch = FetchType.EAGER, mappedBy = "chatUser", cascade = CascadeType.ALL)
    @JsonIgnore
    private ChatState chatState;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "chatUser")
    private Set<MainPage> landings = new HashSet<>();

}
