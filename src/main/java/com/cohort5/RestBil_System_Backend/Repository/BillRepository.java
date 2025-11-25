package com.cohort5.RestBil_System_Backend.Repository;

import com.cohort5.RestBil_System_Backend.Model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillRepository extends JpaRepository<Bill, Long > {

}
