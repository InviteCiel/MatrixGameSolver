package org.invite_ciel.study.matrix_games.view;

import org.invite_ciel.study.matrix_games.i18n.I18n;
import org.invite_ciel.study.matrix_games.reduce.ReductionState;
import org.invite_ciel.study.matrix_games.solver.Solver;
import org.invite_ciel.study.matrix_games.solver.math.MutableArray2DRowRealMatrix;
import org.invite_ciel.study.matrix_games.solver.math.MutableRealMatrix;
import org.invite_ciel.study.matrix_games.solver.model.solution.MatrixGameSolution;
import org.invite_ciel.study.matrix_games.solver.model.solution.MixedStrategyMatrixGameSolution;
import org.invite_ciel.study.matrix_games.solver.model.solution.PureStrategyMatrixGameSolution;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;

/**
 * Created by InviteCiel on 22.02.16.
 */
public class SolverForm extends JFrame {
    private MutableRealMatrix model = null;
    private TableModelListener tableModelListener;

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
    private JTextField saddlePointsField;
    private JPanel reductionPanel;
    private JButton reductionButton;
    private JLabel reductionStateLabel;
    private DefaultTableCellRenderer firstColumnRenderer;

    public SolverForm() {
        super(I18n.getString("appLabel"));
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
        reductionButton.addActionListener(e -> {
            updateModel(model.reduce());
            repaintMatrixTable(model);
        });
        initializeMatrixTable();
    }

    private void solveMatrixGame() {
        MatrixGameSolution solution = new Solver(model).solve();
        if (solution.getClass().equals(PureStrategyMatrixGameSolution.class)) {
            switchFormState(SolverFormState.SOLVED_IN_PURE_STRATEGIES);
            pureStrategiesAlphaField.setText(solution.getPlayerAStrategyView());
            pureStrategiesBetaField.setText(solution.getPlayerBStrategyView());
            pureStrategiesGammaField.setText(solution.getGameResultView());
            saddlePointsField.setText(((PureStrategyMatrixGameSolution)solution).getSaddlePointsView());
        } else if (solution.getClass().equals(MixedStrategyMatrixGameSolution.class)) {
            switchFormState(SolverFormState.SOLVED_IN_MIXED_STRATEGIES);
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
                            getColumn(0).setHeaderValue(I18n.getString("strategiesLabel") + " A\\B");

                } else {
                    //Print column index in header
                    matrixTable.getTableHeader().getColumnModel().
                            getColumn(e.getToIndex()).setHeaderValue("" + e.getToIndex());
                }
            }

            @Override
            public void columnRemoved(TableColumnModelEvent e) {
            }

            @Override
            public void columnMoved(TableColumnModelEvent e) {
            }

            @Override
            public void columnMarginChanged(ChangeEvent e) {
            }

            @Override
            public void columnSelectionChanged(ListSelectionEvent e) {
            }
        });

        tableModelListener = e -> {
            if (e.getType() == TableModelEvent.INSERT) {
                for (int i = e.getFirstRow(); i <= e.getLastRow(); i++) {
                    matrixTable.setValueAt("" + (i + 1), i, 0);
                }
            } else if (e.getType() == TableModelEvent.UPDATE &&
                    e.getFirstRow() >= 0 &&
                    e.getLastRow() >= 0 &&
                    (e.getColumn() >= 1 || e.getColumn() == TableModelEvent.ALL_COLUMNS)) {
                validateMatrixTable();
            }
        };
        matrixTable.getModel().addTableModelListener(tableModelListener);
    }

    private void resizeMatrixTable() {
        ((DefaultTableModel) matrixTable.getModel()).setColumnCount((Integer) spinnerB.getValue() + 1);
        ((DefaultTableModel) matrixTable.getModel()).setRowCount((Integer) spinnerA.getValue());
        updateFirstColumnView();
        switchFormState(SolverFormState.DEFAULT);
    }

    private void validateMatrixTable() {
        if (isValidMatrix()) {
            updateModel(new MutableArray2DRowRealMatrix(parseModelArray()));
            switchFormState(SolverFormState.READY_TO_SOLVE);
        } else {
            switchFormState(SolverFormState.INCORRECT_INPUT);
        }
    }

    private double [][] parseModelArray() {
        double[][] modelArray;
        modelArray = new double[matrixTable.getRowCount()][matrixTable.getColumnCount()-1];
        for (int rowIndex = 0; rowIndex < matrixTable.getRowCount(); rowIndex++) {
            for (int columnIndex = 0; columnIndex < matrixTable.getColumnCount() - 1; columnIndex++) {
                String cell = (String) matrixTable.getModel().getValueAt(rowIndex, columnIndex + 1);
                modelArray[rowIndex][columnIndex] = Double.parseDouble(cell);
            }
        }
        return modelArray;
    }

    private void printModelArray(double[][] modelArray) {
        matrixTable.getModel().removeTableModelListener(tableModelListener);
        for (int rowIndex = 0; rowIndex < matrixTable.getRowCount(); rowIndex++) {
            for (int columnIndex = 0; columnIndex < matrixTable.getColumnCount() - 1; columnIndex++) {
                matrixTable.getModel().setValueAt(
                        Double.toString(modelArray[rowIndex][columnIndex]), rowIndex, columnIndex + 1
                );
            }
        }
        matrixTable.getModel().addTableModelListener(tableModelListener);
    }

    private void updateModel(MutableRealMatrix model) {
        this.model = model;
        switchReductionState(model.getReductionState());
    }

    private void repaintMatrixTable(MutableRealMatrix model) {
        spinnerA.setValue(model.getRowDimension());
        spinnerB.setValue(model.getColumnDimension());
        printModelArray(model.getData());
        switchFormState(SolverFormState.READY_TO_SOLVE);
    }

    private boolean isValidMatrix() {
        for (int rowIndex = 0; rowIndex < matrixTable.getRowCount(); rowIndex++) {
            for (int columnIndex = 1; columnIndex < matrixTable.getColumnCount(); columnIndex++) {
                String cell = (String) matrixTable.getModel().getValueAt(rowIndex, columnIndex);
                if (!isNumber(cell)) {
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
        if (str.matches("[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?")) {
            return true;
        }
        return true;
    }

    private void initializeSpinners() {
        spinnerA.setModel(new SpinnerNumberModel(2, 2, 10, 1));
        spinnerB.setModel(new SpinnerNumberModel(2, 2, 10, 1));
        spinnerA.getModel().addChangeListener(e -> resizeMatrixTable());
        spinnerB.getModel().addChangeListener(e -> resizeMatrixTable());
    }

    private void switchFormState(SolverFormState state) {
        incorrectInputPanel.setVisible(state.isShowIncorrectInputPanel());
        gameResultPanel.setVisible(state.isShowGameResultPanel());
        pureStrategyPanel.setVisible(state.isShowPureStrategyPanel());
        mixedStrategyPanel.setVisible(state.isShowMixedStrategyPanel());
        solveMatrixGameButton.setEnabled(state.isSolveButtonEnabled());
        reductionPanel.setVisible(state.isShowReductionPanel());
        minimizeForm();
    }

    private void minimizeForm() {
        setMinimumSize(new Dimension(300, 0));
        pack();
        setMinimumSize(new Dimension(300, getHeight()));
    }

    private void switchReductionState(ReductionState state) {
        reductionStateLabel.setText(state.getDescription());
        reductionButton.setVisible(state.isShowReduceButton());
        minimizeForm();
    }

    public static void main(String[] args) {
        new SolverForm();
    }
}
