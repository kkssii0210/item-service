package hello.itemservice.domain.item;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItemRepositoryV2 extends JpaRepository<Item, Long> {

}
