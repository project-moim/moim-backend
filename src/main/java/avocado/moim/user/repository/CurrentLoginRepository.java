package avocado.moim.user.repository;

import avocado.moim.user.entity.CurrentLogin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrentLoginRepository extends JpaRepository<CurrentLogin, String> {

    void deleteByEmail(String email);
}
