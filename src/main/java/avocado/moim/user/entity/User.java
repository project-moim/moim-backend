package avocado.moim.user.entity;

import avocado.moim.util.BaseTimeEntity;
import avocado.moim.util.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String password;

    private String name;

    private String address;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    private int status;

    @Builder
    public User(Long id, String email, String password, String name, String address, Role role, int status) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.address = address;
        this.role = role;
        this.status = status;
    }

    //    public List<String> getRoleList() {
//        if (this.roles.length() > 0) {
//            return Arrays.asList(this.roles.split(","));  // ,로 유저 역할 구분
//        }
//        return new ArrayList<>();
//    }
}
