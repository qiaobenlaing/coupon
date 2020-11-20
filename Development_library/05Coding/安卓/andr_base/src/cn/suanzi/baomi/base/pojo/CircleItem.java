package cn.suanzi.baomi.base.pojo;

public class CircleItem {
	private String name;
	private String value;
	private String moduleValue;
	public CircleItem() {
		super();
	}
	
	public CircleItem(String name, String value, String moduleValue) {
		super();
		this.name = name;
		this.value = value;
		this.moduleValue = moduleValue;
	}

	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getModuleValue() {
		return moduleValue;
	}

	public void setModuleValue(String moduleValue) {
		this.moduleValue = moduleValue;
	}

	@Override
	public String toString() {
		return "CircleItem [name=" + name + ", value=" + value + "]";
	}
	
}
