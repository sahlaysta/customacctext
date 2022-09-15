/*
 * MIT License
 * 
 * Copyright (c) 2022 sahlaysta
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package sahlaysta.swing.customacctext;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.plaf.ButtonUI;
import javax.swing.plaf.basic.BasicGraphicsUtils;
import javax.swing.plaf.basic.BasicMenuItemUI;

/**
 * Provides static methods that allow setting custom accelerator text to JMenuItems.
 * 
 * @author sahlaysta
 */
public final class CustomAcceleratorText {
	
	private CustomAcceleratorText() { }
	
	/**
	 * Sets custom accelerator text to a JMenuItem.
	 * 
	 * @param jMenuItem       the JMenuItem
	 * @param acceleratorText the accelerator text
	 * @throws NullPointerException if {@code jMenuItem} is {@code null}
	 */
	public static void setCustomAcceleratorText(
			JMenuItem jMenuItem, String acceleratorText)
				throws NullPointerException {
		removeCustomAcceleratorText(jMenuItem);
		jMenuItem.setUI(new CustomMenuItemUI(jMenuItem.getUI(), acceleratorText));
	}
	
	/**
	 * Gets the custom accelerator text of a JMenuItem, or {@code null} if none
	 * 
	 * @param jMenuItem the JMenuItem
	 * @return the custom accelerator text of a JMenuItem
	 * @throws NullPointerException if {@code jMenuItem} is {@code null}
	 */
	public static String getCustomAcceleratorText(
			JMenuItem jMenuItem)
				throws NullPointerException {
		ButtonUI bui = jMenuItem.getUI();
		if (bui instanceof CustomMenuItemUI)
			return ((CustomMenuItemUI)bui).accText;
		else
			return null;
	}
	
	/**
	 * Removes custom accelerator text from a JMenuItem.
	 * 
	 * @param jMenuItem the JMenuItem
	 * @throws NullPointerException if {@code jMenuItem} is {@code null}
	 */
	public static void removeCustomAcceleratorText(
			JMenuItem jMenuItem)
				throws NullPointerException {
		ButtonUI bui = jMenuItem.getUI();
		if (bui instanceof CustomMenuItemUI)
			jMenuItem.setUI(((CustomMenuItemUI)bui).originalUI);
	}
	

		
	//custom menu item ui
	//(this code here is exactly the same as the original BasicMenuItemUI,
	//the only difference is that the accelerator text is changed)
	//(reflection was used to be able to use sun.swing package)
	static final class CustomMenuItemUI extends BasicMenuItemUI {
		
		final ButtonUI originalUI;
		final String accText;
		CustomMenuItemUI(ButtonUI originalUI, String accText) {
			this.originalUI = originalUI;
			this.accText = accText == null ? "" : accText;
		}
		
		@Override
		protected void paintMenuItem(
				Graphics g, JComponent c, Icon checkIcon,
				Icon arrowIcon, Color background,
				Color foreground, int defaultTextIconGap) {
			BasicMenuItemUI bmiui = (BasicMenuItemUI)new JMenuItem().getUI();
			Font holdf = g.getFont();
			Color holdc = g.getColor();
			JMenuItem mi = (JMenuItem)c;
			g.setFont(mi.getFont());
			Rectangle viewRect = new Rectangle(
				0, 0, mi.getWidth(), mi.getHeight());
			APPLY_INSETS.call(bmiui, viewRect, mi.getInsets());
			Object isLeftToRight =
				IS_LEFT_TO_RIGHT.call(null, mi);
			Object useCheckAndArrow =
				USE_CHECK_AND_ARROW.call(null, menuItem);
			KeyStroke ks = mi.getAccelerator();
			mi.setAccelerator(null);
			Object lh = MENU_ITEM_LAYOUT_HELPER.call(
				null, mi, checkIcon, arrowIcon, viewRect,
				defaultTextIconGap, acceleratorDelimiter,
				isLeftToRight, mi.getFont(), acceleratorFont,
				useCheckAndArrow, getPropertyPrefix());
			mi.setAccelerator(ks);
			ACC_TEXT.call(lh, accText);
			CALC_WIDTHS_AND_HEIGHTS.call(lh);
			SET_ORIGINAL_WIDTHS.call(lh);
			CALC_MAX_WIDTHS.call(lh);
			LEADING_GAP.call(lh,
				(int)GET_LEADING_GAP2.call(lh,
					getPropertyPrefix()));
			CALC_MAX_TEXT_OFFSET.call(lh, viewRect);
			Object lr = LAYOUT_RESULT.call(lh);
			PAINT_BACKGROUND.call(bmiui, g, mi, background);
			PAINT_CHECK_ICON.call(
				bmiui, g, lh, lr, holdc, foreground);
			PAINT_ICON.call(bmiui, g, lh, lr, holdc);
			PAINT_TEXT.call(bmiui, g, lh, lr);
			PAINT_ACC_TEXT.call(bmiui, g, lh, lr);
			PAINT_ARROW_ICON.call(bmiui, g, lh, lr, foreground);
			g.setColor(holdc);
			g.setFont(holdf);
		}
		
		@Override
		protected Dimension getPreferredMenuItemSize(
				JComponent c, Icon checkIcon, Icon arrowIcon,
				int defaultTextIconGap) {
			JMenuItem mi = (JMenuItem) c;
			Object maxRect = CREATE_MAX_RECT.call(null);
			Object isLeftToRight =
				IS_LEFT_TO_RIGHT.call(null, mi);
			Object useCheckAndArrow =
				USE_CHECK_AND_ARROW.call(null, menuItem);
			KeyStroke ks = mi.getAccelerator();
			mi.setAccelerator(null);
			Object lh = MENU_ITEM_LAYOUT_HELPER.call(
				null, mi, checkIcon, arrowIcon, maxRect,
				defaultTextIconGap, acceleratorDelimiter,
				isLeftToRight, mi.getFont(), acceleratorFont,
				useCheckAndArrow, getPropertyPrefix());
			mi.setAccelerator(ks);
			ACC_TEXT.call(lh, accText);
			CALC_WIDTHS_AND_HEIGHTS.call(lh);
			SET_ORIGINAL_WIDTHS.call(lh);
			CALC_MAX_WIDTHS.call(lh);
			LEADING_GAP.call(lh,
				(int)GET_LEADING_GAP2.call(lh,
					getPropertyPrefix()));
			CALC_MAX_TEXT_OFFSET.call(lh, maxRect);
			Dimension result = new Dimension();
			result.width = GET_LEADING_GAP1.call(lh);
			ADD_MAX_WIDTH.call(null,
				GET_CHECK_SIZE.call(lh),
				GET_AFTER_CHECK_ICON_GAP.call(lh),
				result);
			if ((!(boolean)IS_TOP_LEVEL_MENU
					.call(lh))
					&& ((int)GET_MIN_TEXT_OFFSET
						.call(lh) > 0)
					&& ((int)GET_MIN_TEXT_OFFSET
						.call(lh) > result.width)) {
				result.width = GET_MIN_TEXT_OFFSET.call(lh);
			}
			ADD_MAX_WIDTH.call(null,
				GET_LABEL_SIZE.call(lh),
				GET_GAP.call(lh),
				result);
			ADD_MAX_WIDTH.call(null,
				GET_ACC_SIZE.call(lh),
				GET_GAP.call(lh),
				result);
			ADD_MAX_WIDTH.call(null,
				GET_ARROW_SIZE.call(lh),
				GET_GAP.call(lh),
				result);
			result.height = MAX.call(null,
				new int[] {
					GET_HEIGHT.call(
						GET_CHECK_SIZE.call(lh)),
					GET_HEIGHT.call(
						GET_LABEL_SIZE.call(lh)),
					GET_HEIGHT.call(
						GET_ACC_SIZE.call(lh)),
					GET_HEIGHT.call(
						GET_ARROW_SIZE.call(lh))
				}
			);
			Insets insets = ((JMenuItem)
				GET_MENU_ITEM.call(lh)).getInsets();
			if (insets != null) {
				result.width += insets.left + insets.right;
				result.height += insets.top + insets.bottom;
			}
			if (result.width % 2 == 0) {
				result.width++;
			}
			if (result.height % 2 == 0 && Boolean.TRUE !=
					UIManager.get(getPropertyPrefix()
						+ ".evenHeight")) {
				result.height++;
			}
			return result;
		}
		
	}

	
	
	// reflection
	
	static interface Callable {
		Object call(Object obj, Object... args) throws Exception;
	}
	
	static final class Caller {
		final Callable c;
		Caller(Callable c) {
			this.c = c;
		}
		<T> T call(Object obj, Object... args) {
			try {
				@SuppressWarnings("unchecked")
				T t = (T)c.call(obj, args);
				return t;
			} catch (Exception e) {
				throw new Error(e);
			}
		}
	}
	
	static Class<?> reflClass(String className) {
		try {
			return Class.forName(className);
		} catch (Exception e) {
			throw new Error(e);
		}
	}
	
	static Caller reflMethod(
			Class<?> clazz, String methodName, Class<?>... params) {
		try {
			Method m = clazz.getDeclaredMethod(methodName, params);
			m.setAccessible(true);
			return new Caller((o, a) -> m.invoke(o, a));
		} catch (Exception e) {
			throw new Error(e);
		}
	}
	
	static Caller reflConstructor(Class<?> clazz, Class<?>... params) {
		try {
			Constructor<?> con = clazz.getDeclaredConstructor(params);
			con.setAccessible(true);
			return new Caller((o, a) -> con.newInstance(a));
		} catch (Exception e) {
			throw new Error(e);
		}
	}
	
	static Caller reflField(Class<?> clazz, String fieldName) {
		try {
			Field f = clazz.getDeclaredField(fieldName);
			f.setAccessible(true);
			return new Caller((o, a) -> {
				if (a == null || a.length == 0) {
					return f.get(o);
				} else {
					if (a.length != 1) {
						throw new IllegalArgumentException(
							"Cannot set field to multiple arguments");
					}
					f.set(o, a[0]);
					return null;
				}
			});
		} catch (Exception e) {
			throw new Error(e);
		}
	}
	
	private static final Class<?>
		MILH = reflClass("sun.swing.MenuItemLayoutHelper"),
		MILH_LR = reflClass("sun.swing.MenuItemLayoutHelper$LayoutResult"),
		MILH_RS = reflClass("sun.swing.MenuItemLayoutHelper$RectSize");
	private static final Caller
		ACC_TEXT = reflField(MILH, "accText"),
		LEADING_GAP = reflField(MILH, "leadingGap"),
		APPLY_INSETS = reflMethod(
			BasicMenuItemUI.class, "applyInsets",
			Rectangle.class, Insets.class),
		IS_LEFT_TO_RIGHT = reflMethod(
			BasicGraphicsUtils.class, "isLeftToRight", Component.class),
		USE_CHECK_AND_ARROW = reflMethod(
			MILH, "useCheckAndArrow", JMenuItem.class),
		LAYOUT_RESULT = reflMethod(MILH, "layoutMenuItem"),
		PAINT_CHECK_ICON = reflMethod(
			BasicMenuItemUI.class, "paintCheckIcon",
			Graphics.class, MILH,
			MILH_LR, Color.class, Color.class),
		PAINT_ICON = reflMethod(
			BasicMenuItemUI.class, "paintIcon",
			Graphics.class, MILH, MILH_LR, Color.class),
		PAINT_TEXT = reflMethod(
			BasicMenuItemUI.class, "paintText",
			Graphics.class, MILH, MILH_LR),
		PAINT_ACC_TEXT = reflMethod(
			BasicMenuItemUI.class, "paintAccText",
			Graphics.class, MILH, MILH_LR),
		PAINT_ARROW_ICON = reflMethod(
			BasicMenuItemUI.class, "paintArrowIcon",
			Graphics.class, MILH, MILH_LR, Color.class),
		CREATE_MAX_RECT = reflMethod(MILH, "createMaxRect"),
		GET_LEADING_GAP1 = reflMethod(MILH, "getLeadingGap"),
		ADD_MAX_WIDTH = reflMethod(
			MILH, "addMaxWidth",
			MILH_RS, int.class, Dimension.class),
		GET_CHECK_SIZE = reflMethod(MILH, "getCheckSize"),
		GET_AFTER_CHECK_ICON_GAP = reflMethod(
			MILH, "getAfterCheckIconGap"),
		IS_TOP_LEVEL_MENU = reflMethod(MILH, "isTopLevelMenu"),
		GET_MIN_TEXT_OFFSET = reflMethod(MILH, "getMinTextOffset"),
		GET_LABEL_SIZE = reflMethod(MILH, "getLabelSize"),
		GET_ACC_SIZE = reflMethod(MILH, "getAccSize"),
		GET_ARROW_SIZE = reflMethod(MILH, "getArrowSize"),
		GET_GAP = reflMethod(MILH, "getGap"),
		GET_MENU_ITEM = reflMethod(MILH, "getMenuItem"),
		MAX = reflMethod(MILH, "max", int[].class),
		GET_HEIGHT = reflMethod(MILH_RS, "getHeight"),
		CALC_WIDTHS_AND_HEIGHTS = reflMethod(
			MILH, "calcWidthsAndHeights"),
		SET_ORIGINAL_WIDTHS = reflMethod(
			MILH, "setOriginalWidths"),
		CALC_MAX_WIDTHS = reflMethod(
			MILH, "calcMaxWidths"),
		GET_LEADING_GAP2 = reflMethod(
			MILH, "getLeadingGap", String.class),
		CALC_MAX_TEXT_OFFSET = reflMethod(
			MILH, "calcMaxTextOffset", Rectangle.class),
		PAINT_BACKGROUND = reflMethod(
			BasicMenuItemUI.class, "paintBackground",
			Graphics.class, JMenuItem.class, Color.class),
		MENU_ITEM_LAYOUT_HELPER = reflConstructor(
			MILH,
			JMenuItem.class, Icon.class, Icon.class,
			Rectangle.class, int.class, String.class,
			boolean.class, Font.class, Font.class,
			boolean.class, String.class);
	
}