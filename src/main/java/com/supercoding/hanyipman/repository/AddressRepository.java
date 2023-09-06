package com.supercoding.hanyipman.repository;

import com.supercoding.hanyipman.entity.Address;
import com.supercoding.hanyipman.entity.Buyer;
import com.supercoding.hanyipman.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    List<Address> findAllByBuyer(Buyer buyer);

    @Query("select count(a) from Address a join a.buyer b  where b.id=:buyer_id")
    Integer findAllByUserCountId(@Param("buyer_id") Long buyerId);


    @Query("select count(a) from Address a where a.address=:address")
    Integer existsAllByAddress(@Param("address") String address);

    //    @Query("select a,b from Address a  JOIN FETCH Buyer b where a.buyer.id = :user")
//    List<Address> findAllByGetAddressList(@Param("user") User user);
}
