package offer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class Solution {

	public static void main(String[] args) {

		int[] pre = {1, 2, 3, 4, 5, 6, 7};
		int[] in = {3, 2, 4, 1, 6, 5, 7};
 		System.out.println( reConstructBinaryTree(pre, in) );
		
	}
	
	/**
	 * 剑指offer（重建二叉树）
	 * @param pre 前序遍历 
	 * @param in  中序遍历 
	 * @return 树的根结点 
	 */
	public static TreeNode reConstructBinaryTree(int [] pre,int [] in) {
		
		//第一步都需要判断输入合法性，两个数组都不能为空，并且都有数据，而且数据的数目相同
		if( pre == null || in == null || pre.length != in.length || pre.length < 1 )
			return null;
		return construct(pre, 0, pre.length - 1, in, 0, in.length - 1);
    }
	
	/**
	 * 递归重建二叉树
	 * @param pre 前序遍历 
	 * @param ps  前序遍历的开始位置 
	 * @param pe  前序遍历的结束位置 
	 * @param in  中序遍历 
	 * @param is  中序遍历的开始位置 
	 * @param ie  中序遍历的结束位置 
	 * @return 树的根结点 
	 */
	public static TreeNode construct(int[] pre, int ps, int pe, int[] in, int is, int ie) {
		
		// 判断开始位置是否大于结束位置
		if( ps > pe )
			return null;

		//前序遍历第一个位置，即为根节点
		int value = pre[ps];
		//创建当前的根节点，并且为节点赋值
		TreeNode treeNode = new TreeNode(value);
		if( ps == pe )
			return treeNode;
		
		
		int index = is;//中序遍历的开始位置
		
		//寻找中序遍历的根节点
		while( index <= ie && in[index] != value ) {
			index++;
		}
		
		if( index > ie )
			throw new RuntimeException("Invaild input");
		
		treeNode.left = construct(pre, ps + 1, ps + index - is, in, is, index - 1);
		
		treeNode.right = construct(pre, pe - ie + index + 1, pe, in, index + 1, ie);
		
		// 返回创建的根结点  
		return treeNode;
	}
	
	
	/** 
     * 结点对象 
     */  
    public static class ListNode {  
        int val; // 结点的值  
        ListNode next; // 下一个结点  
    }
    
    /**
	 * 剑指offer（从尾到头打印链表）
	 * @param listNode
	 * @return
	 */
	public static List<Integer> printListFromTailToHeadThird(ListNode listNode) {
		
		List<Integer> list = new ArrayList<Integer>();
		
		if( listNode == null )
            return list;
		
        ListNode temp = listNode;
        
        while(temp != null){
        	list.add( temp.val );
            temp = temp.next;
        }
        Collections.reverse(list);
        return list;
    }
    
    /**
	 * 剑指offer（从尾到头打印链表）
	 * @param listNode
	 * @return
	 */
	public static List<Integer> printListFromTailToHeadSecond(ListNode listNode) {
		
		List<Integer> list = new ArrayList<Integer>();
		
		if( listNode == null )
            return list;
		
        Stack<Integer> stack = new Stack<Integer>();
        ListNode temp = listNode;
        
        while(temp != null){
        	stack.push( temp.val );
            temp = temp.next;
        }
        while( !stack.empty() ){
            list.add( stack.pop() );
        }
        return list;
    }
    
	/**
	 * 剑指offer（从尾到头打印链表）
	 * @param listNode
	 * @return
	 */
	public static List<Integer> printListFromTailToHead(ListNode listNode) {
        List<Integer> list = new ArrayList<Integer>();
        if( listNode != null )
        	println(listNode, list);
        return list;
    }
	
	/**
	 * 递归输出
	 * @param listNode
	 * @param list
	 */
	private static void println(ListNode listNode, List<Integer> list){
        if( listNode.next != null )
        {
            println(listNode.next, list);
        }
        list.add(listNode.val);
    }
	
	
	/**
	 * 剑指offer（替换空格）
	 * @param str
	 * @return
	 */
	public static String replaceSpaceFourth(StringBuffer str) {
		
		char[] charArray = str.toString().toCharArray();//转换为字符串然后转化为字符数组
        StringBuilder sBuilder = new StringBuilder();
        //遍历字符数组
        for (char c : charArray) 
        {
            if(c == ' ') 
            {
	            sBuilder.append("%20");    
            } else 
            {
                sBuilder.append(c);
            }
        }
        return sBuilder.toString();
    }
	
	/**
	 * 剑指offer（替换空格）
	 * @param str
	 * @return
	 */
	public static String replaceSpaceThird(StringBuffer str) {
		
		//判断输入是否为空
		if( str == null )
			return null;
		
		int count = 0;//空格个数
		for( int i = 0; i < str.length(); i++ )
		{
			if( str.charAt(i) == ' ')
				count++;
		}
		
		//如果没有空格则直接返回
		if( count == 0 )
			return str.toString();
		
		int len = str.length();//字符总长度
		int newLen = 2 * count + len;//替换之后的总长度
		int index = newLen - 1;//最后一位下标
		
		char singleChar;//存放每一个取出来的字符
		char[] ptr = new char[newLen];//创建新的char数组，用于存放新的字符
		
		while( len > 0 ) {
			singleChar = str.charAt(len - 1);//
			if( singleChar != ' ' ) 
			{
				ptr[index--] = singleChar;
			} else 
			{
				ptr[index--] = '0';
				ptr[index--] = '2';
				ptr[index--] = '%';
			}
			--len;
		}
		return new String(ptr);
    }
	
	/**
	 * 剑指offer（替换空格）
	 * @param str
	 * @return
	 */
	public static String replaceSpaceSecond(StringBuffer str) {

		//判断输入是否为空
		if( str == null )
			return null;
		
		List<Integer> list = new ArrayList<Integer>();
		for( int i = 0; i < str.length(); i++ )
		{
			if( str.charAt(i) == ' ')
				list.add(i);
		}
		Collections.reverse(list);
		for( Integer index : list ) 
		{
			str.replace(index, index + 1, "%20");
		}
		return str.toString();
    }
	
	/**
	 * 剑指offer（替换空格）
	 * @param str
	 * @return
	 */
	public static String replaceSpace(StringBuffer str) {
		
		//判断输入是否为空
		if( str == null )
			return null;
		
		return str.toString().replace(" ", "%20");
    }
	
	/**
	 * 剑指offer（二维数组中的查找）
	 * @param target
	 * @param array
	 * @return
	 */
	public static boolean Find(int target, int [][] array) {
		
		int rows = array.length;//总行数
		int columns = array[0].length;//总列数
		
		if( array != null && rows > 0 && columns > 0
				&& target >= array[0][0] && target <= array[rows -1][columns - 1])
		{
			int row = 0;//循环到的行数
			int column = columns - 1;//循环到的列数
			int rowSec;//中间行
			int colSec;//中间列
			
			while( row < rows && column >= 0 ) {
				
				//相等则返回true
				if( array[row][column] == target )
					return true;
				
				//判断该数是否大于右上角的数
				if( target > array[row][column] )
				{
					rowSec = (int) Math.ceil((row + rows)/2);//取中间一行的行数(首尾行的平均数向上取整)
					if( target > array[rowSec][column] )//判断该值是否大于中间行数最后一列的值
						row = rowSec + 1;//大于则可以将以上的行全部舍弃，小于则不作处理按照原思路求解
					else
						++row;//大于则去掉最上一行的比较
					continue;
						
				} else 
				{
					colSec = (int) Math.floor( (column / 2) );//取中间的列数(首尾列的平均数向下取整，首列为0)
					if( target < array[row][colSec] )//判断该值是否小于中间列数的第一行
						column = colSec - 1;//小于则可以将该列后面的列全部舍弃，大于则不作处理按照原来的思路求解
					else
						--column;//小于则去掉最后一列
					continue;
				}
			}
		}
		return false;
    }
	
	/**
	 * 剑指offer（二维数组中的查找）
	 * @param target
	 * @param array
	 * @return
	 */
	public static boolean FindTwo(int target, int [][] array) {
		
		int rows = array.length;//总行数
		int columns = array[0].length;//总列数
		
		if( array != null && rows > 0 && columns > 0
				&& target >= array[0][0] && target <= array[rows -1][columns - 1])
		{
			int row = 0;//循环到的行数
			int column = columns - 1;//循环到的列数
			
			while( row < rows && column >= 0 ) {
				
				//相等则返回true
				if( array[row][column] == target )
					return true;
				
				//判断该数是否大于右上角的数
				if( target > array[row][column] )
				{
					++row;//大于则去掉最上一行的比较
				} else 
				{
					--column;//小于则去掉最后一列
				}
			}
		}
		return false;
    }

}
