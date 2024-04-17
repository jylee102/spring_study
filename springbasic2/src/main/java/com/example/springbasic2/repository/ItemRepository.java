package com.example.springbasic2.repository;

import com.example.springbasic2.constant.ItemSellStatus;
import com.example.springbasic2.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

// JPA에서는 Repository 클래스가 Model(데이터베이스와 대화) 역할을 한다.
// Repository 클래스는 JpaRepository<사용할 엔티티 클래스, 해당 엔티티의 PK 타입> 인터페이스를 반드시 상속 받아야 한다.
public interface ItemRepository extends JpaRepository<Item, Long> {
    // select * from item where item_nm = ?
    List<Item> findByItemNm(String itemNm);

    // 퀴즈 1-1: temNm이 “테스트 상품1” 이고 ItemSellStatus가 Sell인 레코드를 구하는 Junit 테스트 코드를 완성하세요.
    // select * from item where item_nm = ? and item_sell_status = ?
    List<Item> findByItemNmAndItemSellStatus(String itemNm, ItemSellStatus itemSellStatus);

    // 퀴즈 1-2: price가 10004~ 10008 사이인 레코드를 구하는 Junit 테스트 코드를 완성하세요.
    // 매개변수 이름은 엔티티 클래스의 필드명과 꼭 똑같이 작성하지 않아도 된다.
    // JPA에서는 매개변수의 순서대로 쿼리 물음표에 값을 바인딩한다.
    // select * from item where price between ? and ?
    List<Item> findByPriceBetween(int price1, int price2);

    // 퀴즈 1-3: regTime이 2023-1-1 12:12:44 이후의 레코드를 구하는 Juinit 테스트 코드를 완성하세요.
    // select * from item where reg_time > ?
    List<Item> findByRegTimeAfter(LocalDateTime localDateTime);

    // 퀴즈 1-4: itemSellStatus가 null이 아닌 레코드를 구하는 Juinit 테스트 코드를 완성하세요.
    // select * from item where item_sell_status is not null
    List<Item> findByItemSellStatusNotNull();

    // 퀴즈 1-5: itemDetail이 설명1로 끝나는 레코드를 구하는 Junit 테스트 코드를 완성하세요.
    // select * from item where item_detal like '%?'
    List<Item> findByItemDetailLike(String itemDetail);

    // 퀴즈 1-6: itemNm이 “테스트 상품1” 또는 itemDetail이 “테스트 상품 상세 설명5”인 레코드를 구하는 Junit 테스트 코드를 작성하세요.
    // select * from item where item_nm = ? or item_detail = ?
    List<Item> findByItemNmOrItemDetail(String itemNm, String itemDetail);

    // 퀴즈 1-7: price가 10,005원보다 작은 레코드를 내림차순으로 구하는  Junit 테스트 코드를 작성하세요.
    // select * from item where price < ? order by price desc
    List<Item> findByPriceLessThanOrderByPriceDesc(int price);

    // JPGL 쿼리 (findBy 메소드로 이름을 짓지 않아도 된다.)
    // select * from item where item_detail = ? (일반 쿼리문은 테이블 기준)
    //@Query("select i from Item i where i.itemDetail = ?1 and i.itemNm = ?2")
    //List<Item> findByItemDetail(String itemDetail, String itemNm);

    //@Query("select i from Item i where i.itemDetail like %?1% order by i.price desc")
    //List<Item> findByItemDetail(String itemDetail);

    //@Query("select i from Item i where i.itemDetail = :itemDetail")
    @Query("select i from Item i where i.itemDetail like %:itemDetail% order by i.price desc")
    List<Item> findByItemDetail(@Param("itemDetail") String itemDetail);

    // 쿼즈 2-1: price가 10005 이상인 레코드를 구하는 @Query 어노테이션을 작성하세요.
    @Query("select i from Item i where i.price >= :price")
    List<Item> getPrice(@Param("price") int price);

    // 쿼즈 2-2: itemNm이 “테스트 상품1” 이고 ItemSellStatus가 Sell인 레코드를 구하는 @Query 어노테이션을 작성하세요.
    @Query("select i from Item i where i.itemNm = :itemNm and i.itemSellStatus = :itemSellStatus")
    List<Item> getItemNmAndItemSellStatus(@Param("itemNm") String itemNm,
                                          @Param("itemSellStatus") ItemSellStatus itemSellStatus);

    // 퀴즈 2-3: id가 7인 상품의 레코드를 구하세요.
    @Query("select i from Item i where i.id = :id")
    List<Item> getItemId(@Param("id") Long id);

    // native query
    @Query(value = "select * from item where item_detail like %:itemDetail% order by price desc", nativeQuery = true)
    List<Item> findByItemDetailNative(@Param("itemDetail") String itemDetail);
}
