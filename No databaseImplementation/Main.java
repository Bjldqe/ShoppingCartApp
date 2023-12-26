package ShoppingCart;

import java.util.*;

public class Main {                
    private static GoodsInformation[] goodsincart =new GoodsInformation[100000];
    private static GoodsInformation[] goods =new GoodsInformation[100000];
    private static int index=0;
    static Scanner input=new Scanner(System.in);

    public static void RemoveProducts(int id,int number){
        int i=0,findGoods=0;
        if(goodsincart[id]!=null){
            while (i<=id) {
                if(goodsincart[i].getId()==id){
                    int result=goodsincart[i].getInventory()-number;
                    if(result==0) {
                        for(int j=i;j<index-1;j++){
                            goodsincart[j]=goodsincart[j+1];            
                            index--;
                            System.out.println("成功移除商品");
                        }
                    }
                    else if(result>0){
                        goodsincart[i].reduceInventory(number);
                        System.out.println("成功移除商品");
                    }
                    else{
                        System.out.println("购物车中该商品数量不足!");
                    }
                findGoods=1;    
                break;
                } 
            i++;
            }
 
        }
        if(findGoods==0){
            System.out.println("未找到该商品!");
        }
    }

    public static void AddToCart(int inventory,int id){
        int findGoods=0;        
        for(int i=0;i<1000;i++){
            if(goods[i]!=null&&goods[i].getId()==id){
                findGoods=1;
                if(inventory>goods[i].getInventory()){
                    System.out.println("商品数量不足,请重新输入!");
                    break;
                }
                else{
                    goodsincart[index]=new GoodsInformation();
                    goodsincart[index].setName(goods[i].getName());
                    goodsincart[index].setPrice(goods[i].getPrice());
                    goodsincart[index].setInventory(inventory);
                    goodsincart[index].setId(id);    
                    goods[i].reduceInventory(inventory);
                    break;
                }
            }
        }
        if(findGoods==0){
            System.out.println("未找到该商品!");
        }
        index++;
    }

    public static void DisplayCart(){
        if(goodsincart[0]==null||index==0){
            System.out.println("购物车为空!");
        }
        else{
            for(int i=0;i<index;i++){
                System.out.print("商品名称:"+goodsincart[i].getName());
                System.out.print("  商品ID:"+goodsincart[i].getId());
                System.out.print("  商品单价:"+goodsincart[i].getPrice());
                System.out.println("  商品数量:"+goodsincart[i].getInventory());
            }
        }
    }

    public static void calculateTotalPrice(){
        double sum=0;
        for(int i=0;i<index;i++){
            sum+=goodsincart[i].getPrice()*goodsincart[i].getInventory();
        }
        System.out.println("商品的总价格为"+sum);
    }

    public static void AddToGoodsShelves(){
        int i=0;
        while(goods[i]!=null){
            i++;
        }
        System.out.println("请分别输入放入货架的商品名称,商品ID,商品价格和商品数量");
        String name = input.next();
        int id = input.nextInt();        
        double price = input.nextDouble();
        int inventory = input.nextInt();
        goods[i]=new GoodsInformation();
        goods[i].setName(name);
        goods[i].setPrice(price);
        goods[i].setInventory(inventory);
        goods[i].setId(id); 

    }
    public static void DisplayGoodsShelves(){
        if(goods[0]==null){
            System.out.println("货架为空!");
        }
        else{
            for(int i=0;goods[i]!=null;i++){
                System.out.print("商品名称:"+goods[i].getName());
                System.out.print("  商品ID:"+goods[i].getId());
                System.out.print("  商品单价:"+goods[i].getPrice());
                System.out.println("  商品库存:"+goods[i].getInventory());
            }
        }
    }

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n欢迎来到购物车管理系统,您可以执行以下五个操作:");
            System.out.println("1. 将商品添加到购物车");
            System.out.println("2. 从购物车中移走商品");
            System.out.println("3. 显示购物车中的商品");
            System.out.println("4. 计算购物车商品总价格");
            System.out.println("5. 增加货架商品");
            System.out.println("6. 显示货架上的商品");
            System.out.println("7. 退出");
            int choice=0;
            choice = input.nextInt();
            switch (choice) {
                case 1:
                    System.out.println("请分别输入放入购物车的商品ID和商品数量");
                    int id = input.nextInt();
                    int inventory = input.nextInt();
                    AddToCart(inventory,id);
                    break;
                case 2:
                    System.out.println("请输入需要移除的商品ID和商品数量:");
                    id = input.nextInt();
                    inventory = input.nextInt();
                    RemoveProducts(id,inventory);
                    break;
                case 3:
                    DisplayCart();
                    break;
                case 4:
                    calculateTotalPrice();
                    break;
                case 5:
                    AddToGoodsShelves();
                    break;
                case 6:
                    DisplayGoodsShelves();
                    break;
                case 7:
                    System.out.println("退出程序.");
                    System.exit(0);
                default:
                    System.out.println("无效的选项，请输入菜单中所给的选项!");
            }
        }
    }
}
