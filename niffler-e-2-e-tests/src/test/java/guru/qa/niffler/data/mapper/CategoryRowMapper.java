package guru.qa.niffler.data.mapper;

import guru.qa.niffler.data.entity.spend.CategoryEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;


/**
 * @author Alexander
 */
public class CategoryRowMapper implements RowMapper<CategoryEntity> {

    public static final CategoryRowMapper instance = new CategoryRowMapper();

    private CategoryRowMapper() {
    }

    @Override
    public CategoryEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        CategoryEntity category = new CategoryEntity();
        category.setId(rs.getObject("id", UUID.class));
        category.setUsername(rs.getString("username"));
        category.setName(rs.getString("name"));
        category.setArchived(rs.getBoolean("archived"));
        return category;
    }
}
