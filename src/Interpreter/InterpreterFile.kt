import Parser.ASTNode
import java.lang.Double
import java.util.*
import kotlin.Boolean
import kotlin.NumberFormatException
import kotlin.RuntimeException
import kotlin.String
import kotlin.math.floor

/**
 * Developer #4 — Interpreter (Execution Engine)
 *
 * يستقبل AST ويمشي عليه ويطبع النتايج
 * بيستخدم Visitor Pattern
 */
class Interpreter {
    // =============================================
    // Symbol Table: للـ Testing والـ Debugging
    // Developer #5 ممكن يستخدمها في الـ Tests
    // =============================================
    // =============================================
    // Symbol Table: بتخزن المتغيرات وقيمها
    // مثال: x=10, y=20
    // =============================================
    val symbolTable: MutableMap<String?, Double?> = HashMap<String?, Double?>()

    // Scanner عشان نقرأ من المستخدم في حالة read
    private val scanner = Scanner(System.`in`)

    // =============================================
    // نقطة الدخول الرئيسية
    // بتاخد List<ASTNode> (البرنامج كامل)
    // =============================================
    fun interpret(statements: MutableList<ASTNode>) {
        for (statement in statements) {
            execute(statement)
        }
    }

    // =============================================
    // execute: بتنفذ أي statement
    // مش بترجع قيمة (void)
    // =============================================
    private fun execute(node: ASTNode) {
        if (node is AssignNode) {
            executeAssign(node as AssignNode?)
        } else if (node is PrintNode) {
            executePrint(node as PrintNode?)
        } else if (node is ReadNode) {
            executeRead(node as ReadNode?)
        } else {
            throw RuntimeError("Unknown statement type: " + node.javaClass.getSimpleName())
        }
    }

    // =============================================
    // evaluate: بتحسب قيمة أي expression
    // بترجع Double
    // =============================================
    private fun evaluate(node: ASTNode): Double {
        // رقم مباشر: مثال 42

        if (node is NumberNode) {
            return (node as NumberNode).value
        }

        // متغير: مثال x
        if (node is VariableNode) {
            val name: String? = (node as VariableNode).name
            if (!symbolTable.containsKey(name)) {
                throw RuntimeError("Undefined variable: '" + name + "'")
            }
            return symbolTable.get(name)!!
        }

        // عملية حسابية: مثال x + 5، a * b
        if (node is BinaryOpNode) {
            return evaluateBinaryOp(node as BinaryOpNode?)
        }

        // عملية سالبة: مثال -x
        if (node is UnaryOpNode) {
            return evaluateUnaryOp(node as UnaryOpNode?)
        }

        throw RuntimeError("Cannot evaluate node: " + node.javaClass.getSimpleName())
    }

    // =============================================
    // تنفيذ الـ Assignment: x := 10
    // =============================================
    private fun executeAssign(node: AssignNode) {
        val value = evaluate(node.expression)
        symbolTable.put(node.variableName, value)
        // Debug (اختياري): System.out.println("[DEBUG] " + node.variableName + " = " + value);
    }

    // =============================================
    // تنفيذ الـ Print: print x
    // =============================================
    private fun executePrint(node: PrintNode) {
        val value = evaluate(node.expression)

        // لو الرقم integer اطبعه من غير فاصلة
        if (value == floor(value) && !Double.isInfinite(value)) {
            println(value.toLong())
        } else {
            println(value)
        }
    }

    // =============================================
    // تنفيذ الـ Read: read x
    // بيقرأ من المستخدم ويخزن في المتغير
    // =============================================
    private fun executeRead(node: ReadNode) {
        print("Enter value for " + node.variableName + ": ")
        try {
            val value = scanner.nextLine().trim { it <= ' ' }.toDouble()
            symbolTable.put(node.variableName, value)
        } catch (e: NumberFormatException) {
            throw RuntimeError("Invalid number entered for variable: " + node.variableName)
        }
    }

    // =============================================
    // حساب العمليات الحسابية
    // =============================================
    private fun evaluateBinaryOp(node: BinaryOpNode): kotlin.Double {
        val left = evaluate(node.left)
        val right = evaluate(node.right)

        when (node.operator) {
            "+" -> return left + right
            "-" -> return left - right
            "*" -> return left * right
            "/" -> {
                if (right == 0.0) {
                    throw RuntimeError("Division by zero!")
                }
                return left / right
            }

            else -> throw RuntimeError("Unknown operator: " + node.operator)
        }
    }

    // =============================================
    // حساب العمليات الأحادية (Unary)
    // مثال: -x أو +x
    // =============================================
    private fun evaluateUnaryOp(node: UnaryOpNode): kotlin.Double {
        val value = evaluate(node.operand)
        when (node.operator) {
            "-" -> return -value
            "+" -> return +value
            else -> throw RuntimeError("Unknown unary operator: " + node.operator)
        }
    }

    fun hasVariable(name: String?): Boolean {
        return symbolTable.containsKey(name)
    }

    fun getVariable(name: String?): kotlin.Double {
        if (!symbolTable.containsKey(name)) {
            throw RuntimeError("Variable not found: " + name)
        }
        return symbolTable.get(name)!!
    }

    // =============================================
    // RuntimeError: Exception خاص بالـ Interpreter
    // =============================================
    class RuntimeError(message: String?) : RuntimeException("[Runtime Error] " + message)
}