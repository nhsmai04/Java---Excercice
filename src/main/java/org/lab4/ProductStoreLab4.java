package org.lab4;

import org.lab3.ScrollBarUI;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductStoreLab4 extends JFrame {
    private ProductDAO productDAO;
    private final List<Product> products = new ArrayList<>();

    private JPanel leftPanel;
    private JPanel gridCardPanel;
    private JPanel selectedCard;
    private JLabel lblLargeImage;
    private JLabel lblLargeName;
    private JLabel lblLargePrice;
    private JLabel lblStatus;
    private JTextArea txtLargeDesc;
    private JTextField txtSearch;
    private boolean databaseReady = true;

    public ProductStoreLab4() {
        setTitle("Lab 4 - Product Database");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 650);
        setLocationRelativeTo(null);

        try {
            productDAO = new ProductDAO();
            productDAO.initializeDatabase();
        } catch (SQLException e) {
            databaseReady = false;
            showDatabaseError(e);
        }

        setupLayout();
        loadProducts("");
    }

    private void setupLayout() {
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(320);
        splitPane.setDividerSize(1);

        leftPanel = new JPanel();
        leftPanel.setBackground(Color.WHITE);
        setupLeftPanel();

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Color.WHITE);
        setupRightPanel(rightPanel);

        splitPane.setLeftComponent(leftPanel);
        splitPane.setRightComponent(rightPanel);
        add(splitPane);
    }

    private void setupLeftPanel() {
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(40, 30, 20, 30));

        lblLargeImage = new JLabel();
        lblLargeImage.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblLargeName = new JLabel("No product selected");
        lblLargeName.setFont(new Font("Arial", Font.BOLD, 20));
        lblLargeName.setForeground(new Color(50, 50, 50));
        lblLargeName.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblLargePrice = new JLabel();
        lblLargePrice.setFont(new Font("Arial", Font.BOLD, 18));
        lblLargePrice.setForeground(new Color(50, 50, 50));
        lblLargePrice.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtLargeDesc = new JTextArea();
        txtLargeDesc.setFont(new Font("Arial", Font.PLAIN, 13));
        txtLargeDesc.setForeground(Color.GRAY);
        txtLargeDesc.setLineWrap(true);
        txtLargeDesc.setWrapStyleWord(true);
        txtLargeDesc.setEditable(false);
        txtLargeDesc.setBackground(Color.WHITE);
        txtLargeDesc.setAlignmentX(Component.LEFT_ALIGNMENT);

        leftPanel.add(lblLargeImage);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        leftPanel.add(lblLargeName);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        leftPanel.add(lblLargePrice);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        leftPanel.add(txtLargeDesc);
    }

    private void setupRightPanel(JPanel rightPanel) {
        JPanel searchPanel = new JPanel(new BorderLayout(10, 0));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 15));

        txtSearch = new JTextField();
        txtSearch.setFont(new Font("Arial", Font.PLAIN, 14));
        txtSearch.addActionListener(e -> loadProducts(txtSearch.getText()));

        JButton btnSearch = new JButton("Search");
        btnSearch.addActionListener(e -> loadProducts(txtSearch.getText()));

        JButton btnReload = new JButton("All");
        btnReload.addActionListener(e -> {
            txtSearch.setText("");
            loadProducts("");
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(btnSearch);
        buttonPanel.add(btnReload);

        searchPanel.add(txtSearch, BorderLayout.CENTER);
        searchPanel.add(buttonPanel, BorderLayout.EAST);

        gridCardPanel = new JPanel(new GridLayout(0, 4, 15, 15));
        gridCardPanel.setBackground(Color.WHITE);
        gridCardPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.setBackground(Color.WHITE);
        wrapperPanel.add(gridCardPanel, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(wrapperPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getVerticalScrollBar().setUI(new ScrollBarUI());
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(12, 0));

        lblStatus = new JLabel(" ");
        lblStatus.setFont(new Font("Arial", Font.PLAIN, 12));
        lblStatus.setForeground(Color.GRAY);
        lblStatus.setBorder(BorderFactory.createEmptyBorder(0, 15, 12, 15));

        rightPanel.add(searchPanel, BorderLayout.NORTH);
        rightPanel.add(scrollPane, BorderLayout.CENTER);
        rightPanel.add(lblStatus, BorderLayout.SOUTH);
    }

    private void loadProducts(String keyword) {
        if (!databaseReady) {
            lblStatus.setText("Database is not ready");
            clearLeftPanel();
            return;
        }

        try {
            products.clear();
            products.addAll(productDAO.searchByNameOrBrand(keyword));
            selectedCard = null;
            renderProductCards();
            lblStatus.setText(products.size() + " product(s) loaded from database");

            if (!products.isEmpty()) {
                updateLeftPanel(products.get(0));
            } else {
                clearLeftPanel();
            }
        } catch (SQLException e) {
            databaseReady = false;
            showDatabaseError(e);
        }
    }

    private void renderProductCards() {
        gridCardPanel.removeAll();
        for (Product product : products) {
            gridCardPanel.add(createProductCard(product));
        }
        gridCardPanel.revalidate();
        gridCardPanel.repaint();
    }

    private JPanel createProductCard(Product product) {
        JPanel card = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(242, 242, 242));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                if (this == selectedCard) {
                    g2d.setColor(new Color(100, 160, 240));
                    g2d.setStroke(new BasicStroke(2.0f));
                    g2d.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 15, 15);
                }
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        card.setPreferredSize(new Dimension(180, 250));

        JPanel topPanel = new JPanel(new GridLayout(2, 1, 2, 2));
        topPanel.setOpaque(false);

        JLabel lblName = new JLabel(product.getName());
        lblName.setFont(new Font("Arial", Font.BOLD, 14));
        lblName.setForeground(new Color(60, 60, 60));

        String description = product.getDescription();
        String shortDesc = description.length() > 28 ? description.substring(0, 26) + "..." : description;
        JLabel lblSub = new JLabel(shortDesc);
        lblSub.setFont(new Font("Arial", Font.PLAIN, 11));
        lblSub.setForeground(Color.LIGHT_GRAY);

        topPanel.add(lblName);
        topPanel.add(lblSub);

        JPanel imgWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        imgWrapper.setOpaque(false);

        JLabel lblImg = new JLabel();
        ImageIcon icon = loadImageIcon(product.getImagePath(), 160, 160);
        if (icon != null) {
            lblImg.setIcon(icon);
        } else {
            lblImg.setText("[ " + product.getName() + " ]");
        }
        imgWrapper.add(lblImg);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);

        JLabel lblBrand = new JLabel(product.getBrand());
        lblBrand.setFont(new Font("Arial", Font.PLAIN, 12));
        lblBrand.setForeground(Color.GRAY);

        JLabel lblPrice = new JLabel(product.getFormattedPrice());
        lblPrice.setFont(new Font("Arial", Font.BOLD, 15));
        lblPrice.setForeground(new Color(40, 40, 40));

        bottomPanel.add(lblBrand, BorderLayout.WEST);
        bottomPanel.add(lblPrice, BorderLayout.EAST);

        card.add(topPanel, BorderLayout.NORTH);
        card.add(imgWrapper, BorderLayout.CENTER);
        card.add(bottomPanel, BorderLayout.SOUTH);

        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                JPanel previousCard = selectedCard;
                selectedCard = card;
                updateLeftPanel(product);

                if (previousCard != null) {
                    previousCard.repaint();
                }
                card.repaint();
            }
        });
        return card;
    }

    private void updateLeftPanel(Product product) {
        lblLargeName.setText(product.getName());
        lblLargePrice.setText(product.getFormattedPrice());
        txtLargeDesc.setText(product.getBrand() + "\n\n" + product.getDescription());

        ImageIcon icon = loadImageIcon(product.getImagePath(), 250, 250);
        if (icon != null) {
            lblLargeImage.setText("");
            lblLargeImage.setIcon(icon);
        } else {
            lblLargeImage.setIcon(null);
            lblLargeImage.setText("[ Big Image ]");
        }
    }

    private void clearLeftPanel() {
        lblLargeImage.setIcon(null);
        lblLargeImage.setText("");
        lblLargeName.setText("No product found");
        lblLargePrice.setText("");
        txtLargeDesc.setText("");
    }

    private ImageIcon loadImageIcon(String imagePath, int width, int height) {
        if (imagePath == null || imagePath.isBlank()) {
            return null;
        }

        URL resource = ProductStoreLab4.class.getResource(imagePath);
        ImageIcon icon = null;

        if (resource != null) {
            icon = new ImageIcon(resource);
        } else if (imagePath.startsWith("/")) {
            Path localImage = Path.of("java", imagePath.substring(1));
            if (Files.exists(localImage)) {
                icon = new ImageIcon(localImage.toString());
            }
        }

        if (icon == null || icon.getIconWidth() <= 0) {
            return null;
        }

        Image scaled = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    private void showDatabaseError(Exception e) {
        String message = """
                Cannot connect to PostgreSQL.

                Please set POSTGRES_URL, POSTGRES_USER, POSTGRES_PASSWORD and add the PostgreSQL JDBC driver to the project.
                Error: %s
                """.formatted(e.getMessage());
        JOptionPane.showMessageDialog(this, message, "Database Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ProductStoreLab4().setVisible(true));
    }
}
