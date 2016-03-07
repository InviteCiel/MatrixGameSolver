package org.invite_ciel.study.matrix_games.view;

import org.invite_ciel.study.matrix_games.solver.math.MutableArray2DRowRealMatrix;
import org.invite_ciel.study.matrix_games.solver.math.MutableRealMatrix;
import org.invite_ciel.study.matrix_games.solver.model.solution.MatrixGameSolution;
import org.invite_ciel.study.matrix_games.solver.model.solution.MixedStrategyMatrixGameSolution;
import org.invite_ciel.study.matrix_games.solver.model.solution.PureStrategyMatrixGameSolution;
import org.invite_ciel.study.matrix_games.solver.Solver;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.util.ResourceBundle;

/**
 * Created by InviteCiel on 22.02.16.
 */
public class SolverForm extends JFrame {
    private MutableRealMatrix model = null;

    private static ResourceBundle labels = ResourceBundle.getBundle("label");
    private JSpinner spinnerA;
    private JSpinner spinnerB;
    private JTable matrixTable;
    private JButton solveMatrixGameButton;
    private JPanel pureStrategyPanel;
    private JPanel mixedStrategyPanel;
    private JPanel contentPanel;
    private JPanel incorrectInputPanel;
    private JPanel gameResultPanel;
    private JTextField pureStrategiesAlphaField;
    private JTextField pureStrategiesBetaField;
    private JTextField pureStrategiesGammaField;
    private JTextField mixedStrategiesPField;
    private JTextField mixedStrategiesQField;
    private JTextField mixedStrategiesGammaField;
    @SuppressWarnings("FieldCanBeLocal") //May be used to determine current state of SolverForm
    private SolverFormState state;
    private DefaultTableCellRenderer firstColumnRenderer;

    public SolverForm() {
        super(labels.getString("appLabel"));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        initializeUI();
    }

    private void initializeUI() {
        setContentPane(contentPanel);
        initializeComponents();
        setVisible(true);
    }

    private void initializeComponents() {
        initializeSpinners();
        solveMatrixGameButton.addActionListener(e -> solveMatrixGame());
        initializeMatrixTable();
    }

    private void solveMatrixGame() {
        MatrixGameSolution solution = new Solver(model).solve();
        if (solution.getClass().equals(PureStrategyMatrixGameSolution.class)) {
            switchState(SolverFormState.SOLVED_IN_PURE_STRATEGIES);
            pureStrategiesAlphaField.setText(solution.getPlayerAStrategyView());
            pureStrategiesBetaField.setText(solution.getPlayerBStrategyView());
            pureStrategiesGammaField.setText(solution.getGameResultView());
        } else if (solution.getClass().equals(MixedStrategyMatrixGameSolution.class)) {
            switchState(SolverFormState.SOLVED_IN_MIXED_STRATEGIES);
            mixedStrategiesPField.setText(solution.getPlayerAStrategyView());
            mixedStrategiesQField.setText(solution.getPlayerBStrategyView());
            mixedStrategiesGammaField.setText(solution.getGameResultView());
        }
    }

    private void initializeMatrixTable() {
        initializeMatrixModel();
        resizeMatrixTable();
        initializeMatrixView();
    }

    private void initializeMatrixView() {
        matrixTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        firstColumnRenderer = new DefaultTableCellRenderer();
        firstColumnRenderer.setBackground(UIManager.getColor("TableHeader.background"));
        firstColumnRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        updateFirstColumnView();
        matrixTable.setRowSelectionAllowed(false);
    }

    private void updateFirstColumnView() {
        matrixTable.getColumnModel().getColumn(0).setMinWidth(110);
        matrixTable.getColumnModel().getColumn(0).setMaxWidth(110);
        matrixTable.getColumnModel().getColumn(0).setCellRenderer(firstColumnRenderer);
    }

    private void initializeMatrixModel() {
        matrixTable.setModel(new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0;
            }
        });

        initializeMatrixTableListeners();
        initializeMatrixTableCellEditor();
    }

    private void initializeMatrixTableCellEditor() {
        JTextField field = new JTextField();
        ((AbstractDocument)field.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                //System.out.println("Replace happens!");
                if (isNumber(fb.getDocument().getText(0, offset) +
                        text +
                        fb.getDocument().getText(offset + length,
                                fb.getDocument().getLength() - offset - length))) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }

            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                //System.out.println("Insert happens!");
                if (isNumber(fb.getDocument().getText(0, offset) + string + fb.getDocument().getText(offset, fb.getDocument().getLength() - offset))) {
                    super.insertString(fb, offset, string, attr);
                }

            }
        });
        matrixTable.setCellEditor(new DefaultCellEditor(field));
    }

    private void initializeMatrixTableListeners() {
        matrixTable.getColumnModel().addColumnModelListener(new TableColumnModelListener() {
            @Override
            public void columnAdded(TableColumnModelEvent e) {
                if (e.getToIndex() == 0) {
                    //First column header
                    matrixTable.getTableHeader().getColumnModel().
                            getColumn(0).setHeaderValue(labels.getString("strategiesLabel") + " A\\B");

                } else {
                    //Print column index in header
                    matrixTable.getTableHeader().getColumnModel().
                            getColumn(e.getToIndex()).setHeaderValue("" + e.getToIndex());
                }
            }

            @Override
            public void columnRemoved(TableColumnModelEvent e) {}

            @Override
            public void columnMoved(TableColumnModelEvent e) {}

            @Override
            public void columnMarginChanged(ChangeEvent e) {}

            @Override
            public void columnSelectionChanged(ListSelectionEvent e) {}
        });

        matrixTable.getModel().addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.INSERT) {
                /*System.out.println("Table model insert! " +
                        e.getFirstRow() + " " +
                        e.getLastRow() + " " +
                        e.getColumn());*/
                for (int i = e.getFirstRow(); i <= e.getLastRow(); i++) {
                    matrixTable.setValueAt("" + (i + 1), i, 0);
                }
            } else if (e.getType() == TableModelEvent.UPDATE &&
                    e.getFirstRow() >= 0 &&
                    e.getLastRow() >= 0 &&
                    (e.getColumn() >= 1 || e.getColumn() == TableModelEvent.ALL_COLUMNS)) {
                /*System.out.println("Table model update! " +
                        e.getFirstRow() + " " +
                        e.getLastRow() + " " +
                        e.getColumn());*/
                validateMatrixTable();
            }
        });
    }

    private void resizeMatrixTable() {
        ((DefaultTableModel) matrixTable.getModel()).setColumnCount((Integer) spinnerB.getValue() + 1);
        ((DefaultTableModel) matrixTable.getModel()).setRowCount((Integer) spinnerA.getValue());
        updateFirstColumnView();
        switchState(SolverFormState.DEFAULT);
    }

    private void validateMatrixTable() {

        if (isValidMatrix()) {
            //System.out.println("Valid matrix!");
            updateModel();
            switchState(SolverFormState.READY_TO_SOLVE);
        } else {
            switchState(SolverFormState.INCORRECT_INPUT);
        }
    }

    private void updateModel() {
        double[][] modelArray;
        modelArray = new double[matrixTable.getRowCount()][matrixTable.getColumnCount()-1];
        for (int rowIndex = 0; rowIndex < matrixTable.getRowCount(); rowIndex++) {
            for (int columnIndex = 0; columnIndex < matrixTable.getColumnCount() - 1; columnIndex++) {
                String cell = (String) matrixTable.getModel().getValueAt(rowIndex, columnIndex + 1);
                modelArray[rowIndex][columnIndex] = Double.parseDouble(cell);
                System.out.println("Row: " + rowIndex + ", column: " + columnIndex + ", cell value: " + cell);
            }
        }
        model = new MutableArray2DRowRealMatrix(modelArray);
        System.out.println(model.toString());
    }

    private boolean isValidMatrix() {
        //System.out.println("Matrix table: " + matrixTable.getRowCount() + " rows, " + matrixTable.getColumnCount() + " columns.");
        for (int rowIndex = 0; rowIndex < matrixTable.getRowCount(); rowIndex++) {
            //System.out.println("Row index: " + rowIndex);
            for (int columnIndex = 1; columnIndex < matrixTable.getColumnCount(); columnIndex++) {
                //System.out.println("Column index: " + columnIndex);
                String cell = (String) matrixTable.getModel().getValueAt(rowIndex, columnIndex);
                //System.out.println("Cell [" + rowIndex + ", " + columnIndex + "]: " + cell);
                if (!isNumber(cell)) {
                    //System.out.println(cell + "is not a number.");
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean isNumber(String str) {
        if (str == null)
            return false;
        if (str.equals(""))
            return false;
        /*if (str.matches("\\-?\\(\\d+|\\(\\d*[.,][\\d]+\\)\\)")) {
            return true;
        }*/
        if ((str.toCharArray()[0] < '0' || str.toCharArray()[0] > '9') && str.toCharArray()[0] != '-')
            return false;
        for (int i = 1; i < str.toCharArray().length; i++) {
            if (str.toCharArray()[i] < '0' || str.toCharArray()[i] > '9')
                return false;
        }
        return true;
    }

    private void initializeSpinners() {
        spinnerA.setModel(new SpinnerNumberModel(2, 2, 10, 1));
        spinnerB.setModel(new SpinnerNumberModel(2, 2, 10, 1));
        spinnerA.getModel().addChangeListener(e -> resizeMatrixTable());
        spinnerB.getModel().addChangeListener(e -> resizeMatrixTable());
    }

    private void switchState(SolverFormState state) {
        this.state = state;
        incorrectInputPanel.setVisible(this.state.isShowIncorrectInputPanel());
        gameResultPanel.setVisible(this.state.isShowGameResultPanel());
        pureStrategyPanel.setVisible(this.state.isShowPureStrategyPanel());
        mixedStrategyPanel.setVisible(this.state.isShowMixedStrategyPanel());
        solveMatrixGameButton.setEnabled(this.state.isSolveButtonEnabled());
        setMinimumSize(new Dimension(300, 0));
        pack();
        setMinimumSize(new Dimension(300, getHeight()));
    }

    public static void main(String[] args) {
        new SolverForm();
    }
}
