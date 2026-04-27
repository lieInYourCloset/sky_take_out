package com.sky.mapper;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {

    List<ShoppingCart> listShoppingCart(ShoppingCart shoppingCart);

    @Update("update shopping_cart set number = #{number} where id = #{id}")
    void updateShoppingCartNumber(ShoppingCart shoppingCart);

    @Insert("insert into shopping_cart (user_id, dish_id, setmeal_id, dish_flavor, name, image, amount, number, create_time)" +
            "values (#{userId}, #{dishId}, #{setmealId}, #{dishFlavor}, #{name}, #{image}, #{amount}, #{number}, #{createTime})")
    void insertShoppingCart(ShoppingCart shoppingCart);

    @Delete("delete from shopping_cart where id = #{id}")
    void deleteShoppingCartById(Long id);

    @Select("select * from shopping_cart where user_id = #{userId} order by create_time asc")
    List<ShoppingCart> getShoppingCartByUserId(Long userId);

    @Delete("delete from shopping_cart where user_id = #{userId}")
    void cleanShoppingCartByUserId(Long userId);
}
