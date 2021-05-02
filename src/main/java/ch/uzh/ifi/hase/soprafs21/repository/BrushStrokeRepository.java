package ch.uzh.ifi.hase.soprafs21.repository;

import ch.uzh.ifi.hase.soprafs21.entity.BrushStroke;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("brushStrokeRepository")
public interface BrushStrokeRepository extends JpaRepository<BrushStroke, Long> {

}
