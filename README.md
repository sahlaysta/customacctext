# customacctext
A library with a fairly hacky solution to set custom accelerator text to JMenuItems. Ought to work on all of Java's official lookandfeels. Tested on different Java versions and platforms.

<img src="https://i.imgur.com/Qi1jekr.png"/>

# Usage

Use the `CustomAcceleratorText.setCustomAcceleratorText(JMenuItem, String)` method to give custom accelerator text to a JMenuItem. If the JMenuItem already has an accelerator, the custom text will replace it.

```java
JMenuBar jMenuBar = new JMenuBar();
JMenu jMenu = new JMenu("Menu");

JMenuItem jMenuItem1 = new JMenuItem("Normal");
jMenuItem1.setAccelerator(KeyStroke.getKeyStroke("ctrl N"));
jMenu.add(jMenuItem1);

JMenuItem jMenuItem2 = new JMenuItem("Item text");
CustomAcceleratorText.setCustomAcceleratorText(jMenuItem2, "Custom accelerator text");
jMenu.add(jMenuItem2);

jMenuBar.add(jMenu);
```
