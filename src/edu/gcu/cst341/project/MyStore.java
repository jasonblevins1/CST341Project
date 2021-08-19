package edu.gcu.cst341.project;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;

public class MyStore {
	
	Scanner sc = new Scanner(System.in);

	private String name; 
	private DBConnect con;

	MyStore (String name){
		this.name = name;
		con = new DBConnect();
	}

	public void open() {
		String user = null;
		boolean exit = false;
		do {
			switch (UserInterface.menuMain()) {
			case 0:
				System.out.println("Thank you! Come again!");
				exit = true;
				break;
			case 1:
				user = login();
				if (user != null) {
					System.out.println("Login successful!!");
					shop();
				}
				else {
					System.out.println("Login unsuccessful");
				}
				break;
			case 2:
				admin();
				break;
			default:
				open();
			}
		} while (!exit);
	}

	private String login() {
		String result = null;

		String [] login = UserInterface.login();

		String sql = "SELECT UserId, UserFirstName FROM users WHERE UserName = ? AND UserPassword = ? AND UserStatus = 1";

		try (PreparedStatement ps = con.getConnection().prepareStatement(sql)){
			ps.setString(1, login[0]);
			ps.setString(2, login[1]);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				result = rs.getString("UserFirstName");
				System.out.println("\n" + "Thank you " + result);
			}
			else {
				result = null;
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private void shop() {
		switch (UserInterface.menuShop()) {
		case 0:
			return;
		case 1:
			createCartItem();
			break;
		case 2:
			readCartItems();
			break;
		case 3:
			deleteCartItem();
			break;
		default:
			return;
		}
	}

	private void admin() {
		switch (UserInterface.menuAdmin()) {
		case 0:
			return;
		case 1:
			createProduct();
			break;
		case 2:
			readProducts();		
			break;
		case 3:
			updateProduct();
			break;
		case 4:
			deleteProduct();
			break;	
		default:
			open();
		}
	}
	
	private void createCartItem() {
		
	}
	
	private void readCartItems() {
		System.out.println("View (Read) cart...");
		System.out.println();
	}
	
	private void deleteCartItem() {
		System.out.println("Delete from cart...");
		System.out.println();
	}
	
	private void createProduct() {
		
		readProducts();
		
		System.out.println("Add Product Id");
		int prodId = sc.nextInt();
		sc.nextLine();
		System.out.println("Add Product Name");
		String prodName = sc.nextLine();
		System.out.println("Add Product Price");
		double prodPrice = sc.nextDouble();
		sc.nextLine();
		System.out.println("Add Product Stock Status");
		String prodStStatus = sc.nextLine();
		
		
		
		String sql = "Insert Into cst341project.products (ProductId, ProductName, ProductPrice, ProductStockStatus) Values (?, ?, ?, ?)";
		
		PreparedStatement stmt = null;
		try {
			stmt = con.getConnection().prepareStatement(sql);
			stmt.setInt(1, prodId);
			stmt.setString(2, prodName);
			stmt.setDouble(3, prodPrice);
			stmt.setString(4, prodStStatus);
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("================================================");
		System.out.println("-----------The Product Has Been Added-----------");
		System.out.println("================================================");
	}
	
	private void readProducts() {
		
		System.out.println("================================================");
		System.out.println("---------------Current Products-----------------");
		System.out.println("================================================");
		System.out.printf("%-20s %-20s %-20s %-20s\n","[Product ID] " , "[Product Name] " , "[Product Price] " , "[Product Stock Status]");
		System.out.println("\n");
		String sql2 = "SELECT ProductId, ProductName, ProductPrice, ProductStockStatus FROM cst341project.products;";
		try (PreparedStatement ps = con.getConnection().prepareStatement(sql2)){
			
			ResultSet rs = ps.executeQuery(sql2);
			
			while(rs.next()) {
				
				System.out.printf("%-20s %-20s %-20s %-20s\n", "[" + rs.getString("ProductId") + "]" , rs.getString("ProductName") ,rs.getString("ProductPrice") , rs.getString("ProductStockStatus") + "\n");
			}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
			
		}
		

	
	// Added to allow upload to group project
	// Chris Stier 08/14/2021
	private void updateProduct() {
		
		
		readProducts();
		
		
		
try {
			
			Class.forName("com.mysql.cj.jdbc.Driver"); // asks for try/catch
			
			String connection = "jdbc:mysql://localhost:3306/cst341project";
			String user = "root";
			String password = "DK90";
			
			Connection con = DriverManager.getConnection(connection, user, password);  // add clause to surrounding try
			
			 //  lines 27-53 INSERT (UPDATE, DELETE)
			
			
			System.out.print("Enter Product Id [123-130]: ");
			Integer ProductId = sc.nextInt();
			sc.nextLine();
			
			System.out.print("Enter Product Name [ex: Toaster]: ");
			String ProductName = sc.nextLine();
			
			System.out.print("Enter Product Price [ex 14.99]: ");
			Double ProductPrice = sc.nextDouble();
			sc.nextLine();
			
			System.out.print("Enter Product Stock Status [in stock: true or false]: ");
			String ProductStockStatus = sc.nextLine();

			String sqlBasic = "UPDATE cst341project.products SET ProductName = ?, ProductPrice = ?, ProductStockStatus = ? WHERE ProductId = ?";
			PreparedStatement stmt = con.prepareStatement(sqlBasic);
			stmt.setString(1, ProductName);
			stmt.setDouble(2, ProductPrice);
			stmt.setString(3, ProductStockStatus);
			stmt.setInt(4, ProductId);
			stmt.executeUpdate();	// for INSERT, UPDATE, DELETE
				
			System.out.println("================================================");
			System.out.println("---------------Product Updated------------------");
			System.out.println("================================================");
			
			stmt.close();
			con.close();
	
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void deleteProduct() {
		
		readProducts();
		try {

			System.out.println("================================================");
			System.out.println("Please select a product to delete by Product Id");
			System.out.println("================================================");
			int ProductId = sc.nextInt();
			sc.nextLine();
			String sqlBasic = "delete from cst341project.products\n" + "  where ProductId = ?;";
			PreparedStatement ps1 = con.getConnection().prepareStatement(sqlBasic);
			ps1.setInt(1, ProductId);
			ps1.executeUpdate();
			System.out.println("================================================");
			System.out.println("--------------Deleting Product......------------");
			System.out.println("================================================");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}



