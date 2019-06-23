package es.ucm.fdi.tp.view.component;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;

public class CenteredSquareLayout implements LayoutManager {
	public void addLayoutComponent(String name, Component comp) {}
	public void removeLayoutComponent(Component comp) {}
	public Dimension preferredLayoutSize(Container parent) {
	return parent.getSize();
	}
	public Dimension minimumLayoutSize(Container parent) {
	return parent.getSize();
	}
	public void layoutContainer(Container parent) {
	int min = Math.min(parent.getWidth(), parent.getHeight());
	int x0 = 0, y0 = 0;
	if (min == parent.getWidth()) {
	y0 = (parent.getHeight() - min) / 2;
	} else {
	x0 = (parent.getWidth() - min) / 2;
	}
	Component child = parent.getComponent(0);
	 child.setBounds(x0, y0, min, min);
	}
}
