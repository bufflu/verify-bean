# verify-bean
工具包，用于自定义 Simple Bean 的校验。

## 使用
校验 TestBean
```java
public static void main(String[] args) {
    Validator.verify(new TestBean());
}
```
默认只做 NOT NULL 校验，可以通过添加注解 @VerifiableBean 灵活配置。
```java
@VerifiableBean(level = VerifiableLevel.NOTBLANK, propagation = VerifiablePropagation.RECURSIVE)
public class TestBean {
    //...
}
```
**propagation**: 可配置 CURRENT （当前）和 RECURSIVE （递归），前者只会校验当前 Bean 的属性，后者可以递归的校验每个 Simple Bean。

**level**: 校验级别， 默认为 NOTNULL，可选 NOTEMTRY、NOTBLANK 和 CUSTOM （自定义）。

**exclude**: 将不加入校验的字段添加到此处。

**customClass**: 自定义校验类，必须实现 ValidatorCustom 接口，且 level 为 CUSTOM 才生效。

## 特别说明
当多个 Bean 为嵌套关系，配置递归校验时，当前 Bean 的注解优先。
```java
@VerifiableBean(level = VerifiableLevel.NOTBLANK, propagation = VerifiablePropagation.RECURSIVE)
public class TestBean {
    
    private TestBeanEdu testBeanEdu = new TestBeanEdu();
    //...
}
```
```java
@VerifiableBean(propagation = VerifiablePropagation.CURRENT)
public class TestBeanEdu {
    
    private TestBeanEduSub sub = new TestBeanEduSub();
    //...
}
```
如 TestBean 配置 RECURSIVE，TestBeanEdu 将会加入校验，而 TestBeanEdu 配置 CURRENT，所以 TestBeanEduSub **不会**加入校验。

## 结尾
写此工具包的初衷时用来解决 JAXB 完成 XML 与 Bean 映射后对 Bean 的校验。