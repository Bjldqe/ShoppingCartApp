package JavaCode.shoppingCartTest2;
import java.sql.*;

import java.util.Scanner;

class Product {
    private String productId;
    private String productName;
    private double price;
    private int stock;
    
class ShoppingCart {
    private Connection connection;

    public ShoppingCart(Connection connection) {
        this.connection = connection;
    }

    public void addToCart(String productId, int quantity) {
        try {
            PreparedStatement checkStockStatement = connection.prepareStatement(
                    "SELECT stock FROM products WHERE product_id = ?");
            checkStockStatement.setString(1, productId);
            ResultSet resultSet = checkStockStatement.executeQuery();

            if (resultSet.next()) {
                int stock = resultSet.getInt("stock");

                if (stock >= quantity) {
                    PreparedStatement addToCartStatement = connection.prepareStatement(
                            "INSERT INTO cart (product_id, quantity) VALUES (?, ?) " +
                                    "ON DUPLICATE KEY UPDATE quantity = quantity + ?");
                    addToCartStatement.setString(1, productId);
                    addToCartStatement.setInt(2, quantity);
                    addToCartStatement.setInt(3, quantity);
                    addToCartStatement.executeUpdate();

                    PreparedStatement updateStockStatement = connection.prepareStatement(
                            "UPDATE products SET stock = stock - ? WHERE product_id = ?");
                    updateStockStatement.setInt(1, quantity);
                    updateStockStatement.setString(2, productId);
                    updateStockStatement.executeUpdate();

                    System.out.println("商品成功添加到购物车");
                } else {
                    System.out.println("该商品已售罄");
                }
            } else {
                System.out.println("未找到该商品");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeFromCart(String productId) {
        try {
            PreparedStatement removeFromCartStatement = connection.prepareStatement(
                    "DELETE FROM cart WHERE product_id = ?");
            removeFromCartStatement.setString(1, productId);
            removeFromCartStatement.executeUpdate();

            System.out.println("商品成功从购物车中移除");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void displayCartItems() {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    "SELECT c.product_id, p.product_name, c.quantity, p.price " +
                            "FROM cart c JOIN products p ON c.product_id = p.product_id");

            System.out.println("购物车商品:");
            while (resultSet.next()) {
                String productId = resultSet.getString("product_id");
                String productName = resultSet.getString("product_name");
                int quantity = resultSet.getInt("quantity");
                double price = resultSet.getDouble("price");

                System.out.println("商品ID:" + productId +
                        ", 名称:" + productName +
                        ", 储量:" + quantity +
                        ", 单价:" + price);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public double calculateTotalPrice() {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    "SELECT SUM(p.price * c.quantity) AS total_price " +
                            "FROM cart c JOIN products p ON c.product_id = p.product_id");

            if (resultSet.next()) {
                return resultSet.getDouble("total_price");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }
}

public class ShoppingCartApp {
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String jdbcUrl = "jdbc:mysql://localhost:3306/ShoppingCart";
            String username = "root";
            String password = "3310309112a";
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);

            createTables(connection);

            ShoppingCart shoppingCart = new ShoppingCart(connection);
            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("\n欢迎来到购物车管理系统,您可以执行以下五个操作:");
                System.out.println("1. 将商品添加到购物车");
                System.out.println("2. 从购物车中移走商品");
                System.out.println("3. 显示购物车中的商品");
                System.out.println("4. 计算购物车商品总价格");
                System.out.println("5. 退出");

                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        System.out.println("请输入商品ID:");
                        String productId = scanner.next();
                        System.out.println("请输入商品数量:");
                        int quantity = scanner.nextInt();
                        shoppingCart.addToCart(productId, quantity);
                        break;
                    case 2:
                        System.out.println("请输入需要移除的商品ID:");
                        productId = scanner.next();
                        shoppingCart.removeFromCart(productId);
                        break;
                    case 3:
                        shoppingCart.displayCartItems();
                        break;
                    case 4:
                        System.out.println("总价格: ￥" + shoppingCart.calculateTotalPrice());
                        break;
                    case 5:
                        System.out.println("退出程序.");
                        connection.close(); 
                        System.exit(0);
                    default:
                        System.out.println("无效的选项，请输入菜单中所给的选项");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static void createTables(Connection connection) {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS products (" +
                            "product_id VARCHAR(10) PRIMARY KEY," +
                            "product_name VARCHAR(255) NOT NULL," +
                            "price DOUBLE NOT NULL," +
                            "stock INT NOT NULL)");
            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS cart (" +
                            "product_id VARCHAR(10) PRIMARY KEY," +
                            "quantity INT NOT NULL)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
