import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

public class GroceryApp {
    private JFrame frame;
    private JTextField productNameField;
    private JComboBox<String> categoryComboBox;
    private JTextArea detailsArea;
    private JList<String> productList;
    private DefaultListModel<String> listModel;
    private JCheckBox onSaleCheckBox;
    private JRadioButton cashRadioButton;
    private JRadioButton creditRadioButton;
    private JLabel statusLabel;
    private ArrayList<String> products;

    public GroceryApp() {
        // Инициализация списка продуктов
        products = new ArrayList<>();

        System.out.println("new changes!");

        frame = new JFrame("Регистрация поступлений в продуктовый магазин");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // Изменение поведения при закрытии окна
        frame.setSize(400, 500);
        frame.setLayout(new BorderLayout());

        listModel = new DefaultListModel<>();

        loadProducts();

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 2));

        panel.add(new JLabel("Название продукта:"));
        productNameField = new JTextField();
        panel.add(productNameField);

        panel.add(new JLabel("Категория:"));
        categoryComboBox = new JComboBox<>(new String[]{"Фрукты", "Овощи", "Молочные продукты", "Выпечка", "Мясные изделия", "Напитки", "Бакалея"});
        panel.add(categoryComboBox);

        onSaleCheckBox = new JCheckBox("На акции");
        panel.add(onSaleCheckBox);

        ButtonGroup paymentGroup = new ButtonGroup();
        cashRadioButton = new JRadioButton("Наличный");
        creditRadioButton = new JRadioButton("Безналичный");
        paymentGroup.add(cashRadioButton);
        paymentGroup.add(creditRadioButton);
        panel.add(cashRadioButton);
        panel.add(creditRadioButton);

        JButton addButton = new JButton("Добавить поступление");
        addButton.addActionListener(e -> addProduct());
        panel.add(addButton);

        productList = new JList<>(listModel);
        productList.addListSelectionListener(e -> showProductDetails());
        JScrollPane listScrollPane = new JScrollPane(productList);

        detailsArea = new JTextArea(5, 20);
        detailsArea.setEditable(false);
        JScrollPane detailsScrollPane = new JScrollPane(detailsArea);


        statusLabel = new JLabel("Статус: Ожидание...");

        // Добавление компонентов в главное окно
        frame.add(panel, BorderLayout.NORTH);
        frame.add(listScrollPane, BorderLayout.CENTER);
        frame.add(detailsScrollPane, BorderLayout.SOUTH);
        frame.add(statusLabel, BorderLayout.PAGE_END);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int confirmed = JOptionPane.showConfirmDialog(frame,
                        "Вы уверены, что хотите выйти?",
                        "Подтверждение выхода",
                        JOptionPane.YES_NO_OPTION);

                if (confirmed == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });


        frame.setVisible(true);
    }

    private void addProduct() {
        // Получение данных о продукте
        String name = productNameField.getText();
        String category = (String) categoryComboBox.getSelectedItem();
        boolean onSale = onSaleCheckBox.isSelected();
        String paymentMethod = cashRadioButton.isSelected() ? "Наличный" : "Безналичный";


        if (!name.isEmpty()) {

            String productEntry = String.format("%s (%s) - %s - %s", name, category, onSale ? "На акции" : "Без акции", paymentMethod);
            listModel.addElement(productEntry); // Добавление продукта в модель списка
            products.add(productEntry); // Добавление продукта в список
            saveProducts(); // Сохранение продуктов в файл
            statusLabel.setText("Статус: Поступление добавлено!"); // Обновление статуса
            productNameField.setText(""); // Очистка поля ввода
            onSaleCheckBox.setSelected(false); // Сброс флажка
            cashRadioButton.setSelected(false); // Сброс радиокнопки
            creditRadioButton.setSelected(false); // Сброс радиокнопки
        } else {
            statusLabel.setText("Статус: Введите название продукта!");
        }
    }

    private void showProductDetails() {
        String selectedProduct = productList.getSelectedValue();
        detailsArea.setText(selectedProduct != null ? selectedProduct : "");
    }

    private void loadProducts() {
        try (BufferedReader reader = new BufferedReader(new FileReader("products.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                listModel.addElement(line);
                products.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveProducts() {
        // Сохранение продуктов в файл
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("products.txt"))) {
            for (String product : products) {
                writer.write(product);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GroceryApp::new);
    }
}