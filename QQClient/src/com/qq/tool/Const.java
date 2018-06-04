package com.qq.tool;

import java.awt.*;
import java.util.Arrays;
import java.util.HashSet;

public class Const {
	public static final int SCREENWIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
	public static final int SCREENHEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;
	public static final Font FONT0 = new Font("黑体", Font.PLAIN, 20);
	public static final Font FONT1 = new Font("宋体", Font.PLAIN, 16);
	public static final Font FONT2 = new Font("宋体", Font.PLAIN, 15);
	public static final Font FONT3 = new Font("宋体", Font.PLAIN, 14);
	public static final Font FONT4 = new Font("黑体", Font.PLAIN, 13);
	public static final Font FONT5 = new Font("宋体", Font.PLAIN, 12);
	public static final Color RANDOMCOLOR = new Color((int) (Math.random() * 256), (int) (Math.random() * 256),
			(int) (Math.random() * 256));
	public static final HashSet<String> NUMTYPE = new HashSet<String>(
			Arrays.asList(new String[] { "bit", "int", "bigint", "float", "numeric" }));
}
