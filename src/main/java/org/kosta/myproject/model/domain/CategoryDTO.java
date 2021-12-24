package org.kosta.myproject.model.domain;

public class CategoryDTO {
	private int categoryId;
	private String categoryName;
	public CategoryDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public CategoryDTO(int categoryId, String categoryName) {
		super();
		this.categoryId = categoryId;
		this.categoryName = categoryName;
	}
	public int getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	@Override
	public String toString() {
		return "CategoryDTO [categoryId=" + categoryId + ", categoryName=" + categoryName + "]";
	}

}
