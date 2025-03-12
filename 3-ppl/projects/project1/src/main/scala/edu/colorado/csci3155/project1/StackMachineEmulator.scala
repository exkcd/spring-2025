package edu.colorado.csci3155.project1
import scala.annotation.tailrec



sealed trait StackMachineInstruction
/*-- Complete the byte code instructions as specified in the documentation --*/
case class LoadEnv(s: String) extends StackMachineInstruction
case class  StoreEnv(s: String) extends StackMachineInstruction
case object PopEnv extends StackMachineInstruction

case class PushNumI(f: Double) extends StackMachineInstruction
case class PushBoolI(b: Boolean) extends StackMachineInstruction
case object AddI extends StackMachineInstruction
case object SubI extends StackMachineInstruction
case object MultI extends StackMachineInstruction
case object DivI extends StackMachineInstruction
case object ExpI extends StackMachineInstruction
case object LogI extends StackMachineInstruction
case object SinI extends StackMachineInstruction
case object CosI extends StackMachineInstruction
case object GeqI extends StackMachineInstruction
case object EqI extends StackMachineInstruction
case object NotI extends StackMachineInstruction
case object PopI extends StackMachineInstruction

case class CSkipI(numToSkip: Int) extends StackMachineInstruction
case class SkipI(numToSkip: Int) extends StackMachineInstruction

object StackMachineEmulator {

    /*-- An environment stack is a list of tuples containing strings and values --*/
    type RuntimeStack = List[(String, Value)]
    /*-- An operand stack is a list of values --*/
    type OpStack = List[Value]

    /* Function emulateSingleInstruction
        Given a list of values to represent a operand stack
        a list of tuples (string, value) to represent runtime stack
        and a single instruction of type StackMachineInstruction
        Return a tuple that contains the
        modified stack that results when the instruction is executed.
        modified runtime that results when the instruction is executed.

        Make sure you handle the error cases: eg., stack size must be appropriate for the instruction
        being executed. Division by zero, log of a non negative number
        Throw an exception or assertion violation when error happens.
     */

    def emulateSingleInstruction(stack: OpStack,
                                env: RuntimeStack,
                                ins: StackMachineInstruction): (OpStack, RuntimeStack) = {
        ins match {
            /*TODO:  Your code here must handle each instruction type and
            execute the appropriate instructions to modify the
            runtime/operand stacks as specified */
            case PushNumI(f) => (Num(f)::stack, env)
            
            case PushBoolI(b) => (Bool(b)::stack, env)
            
            case PopI if (!stack.isEmpty) => (stack.tail, env)

            case AddI if (stack.length >= 2) => {
                if ((stack.tail.length < 1) || (stack.tail.tail.length < 0)) {
                    throw new IllegalArgumentException("Too few arguments")
                }
                val v1 = stack.head.getDoubleValue
                val v2 = stack.tail.head.getDoubleValue
                val new_stack = Num(v1+v2)::stack.tail.tail
                (new_stack, env)
            }
            case SubI if (stack.length >= 2) => {
                if ((stack.tail.length < 1) || (stack.tail.tail.length < 0)) {
                    throw new IllegalArgumentException("Too few arguments")
                }
                val v1 = stack.head.getDoubleValue
                val v2 = stack.tail.head.getDoubleValue
                val new_stack = (Num(v2-v1))::stack.tail.tail
                (new_stack, env)
            }
            case MultI if (stack.length >= 2) => {
                if ((stack.tail.length < 1) || (stack.tail.tail.length < 0)) {
                    throw new IllegalArgumentException("Too few arguments")
                }
                val v1 = stack.head.getDoubleValue
                val v2 = stack(1).getDoubleValue
                val new_stack = (Num(v1*v2))::stack.tail.tail
                (new_stack, env)
            }
            case DivI if (stack.length >= 2) => {
                if ((stack.tail.length < 1) || (stack.tail.tail.length < 0)) {
                    throw new IllegalArgumentException("Too few arguments")
                }
                val v1 = stack.head.getDoubleValue
                val v2 = stack(1).getDoubleValue
                val new_stack = stack.tail.tail
                if (v1 == 0) { throw new ArithmeticException("Division by zero.") }
                else { (Num(v2/v1)::new_stack, env) }
            }
            case LogI if (stack.length >= 1) => {
                val v1 = stack.head.getDoubleValue
                (Num(scala.math.log(v1))::stack.tail, env)
            }
            case ExpI if (stack.length >= 1) => {
                val v1 = stack.head.getDoubleValue
                (Num(scala.math.exp(v1))::stack.tail, env)
            }
            case SinI if (stack.length >= 1) => {
                val v1 = stack.head.getDoubleValue
                (Num(scala.math.sin(v1))::stack.tail, env)
            }
            case CosI if (stack.length >= 1)=> {
                val v1 = stack.head.getDoubleValue
                (Num(scala.math.cos(v1))::stack.tail, env)
            }
            case GeqI if (stack.length >= 2) => {
                if ((stack.tail.length < 1) || (stack.tail.tail.length < 0)) {
                    throw new IllegalArgumentException("Too few arguments")
                }
                val v1 = stack.head.getDoubleValue
                val v2 = stack.tail.head.getDoubleValue
                val new_stack = stack.tail.tail
                val bool = Bool(v2>=v1).getBooleanValue
                (Bool(bool)::new_stack, env)

            }
            case EqI if (stack.length >= 2) => {
                if ((stack.tail.length < 1) || (stack.tail.tail.length < 0)) {
                    throw new IllegalArgumentException("Too few arguments")
                }
                val v1 = stack.head
                val v2 = stack.tail.head
                val new_stack = stack.tail.tail
                (v1, v2) match {
                    case (Num(v1), Num(v2)) => (Bool(v2==v1)::new_stack, env)
                    case (Bool(v1), Bool(v2)) => (Bool(v2==v1)::new_stack, env)
                    case _ => throw new RuntimeException("no")
                }
            }
            case NotI if (!stack.isEmpty) => {
                val v1 = stack.head
                v1 match {
                    case Bool(v1) => (Bool(!v1)::stack.tail, env)
                    case _ => throw new RuntimeException("no")
                }
            }
            case LoadEnv(s) => (stack.tail, (s, stack.head)::env)
            case PopEnv => (stack, env.tail)
            case StoreEnv(s) => {
                val v1 = env.find(x => x._1 == s)
                val v2 = v1 match {
                    case Some(s) => s
                    case None => throw new RuntimeException("Can't find identifier")
                }
                (v2._2::stack, env)
            }
            case _ => throw new RuntimeException(s"Unknown instruction type: $ins")
        }
    }

    /* Function emulateStackMachine
    Execute the list of instructions provided as inputs using the
    emulateSingleInstruction function.
    Return the final runtimeStack and the top element of the opStack
     */
    @tailrec
    def emulateStackMachine(instructionList: List[StackMachineInstruction],
                            opStack: OpStack=Nil,
                            runtimeStack: RuntimeStack=Nil): (Value, RuntimeStack) =
        {
            /*-- Are we out of instructions to execute --*/
            if (instructionList.isEmpty){
                /*-- output top elt. of operand stack and the runtime stack --*/
                (opStack.head, runtimeStack)
            } else {
                /*- What is the instruction on top -*/
                val ins = instructionList.head
                ins match {
                    /*-- Conditional skip instruction --*/
                    case CSkipI(n) => {
                        /* get the top element in operand stack */
                        val topElt = opStack.head
                        val restOpStack = opStack.tail
                        val b = topElt.getBooleanValue /* the top element better be a boolean */
                        if (!b) {
                            /*-- drop the next n instructions --*/
                            val restOfInstructions = instructionList.drop(n+1)
                            emulateStackMachine(restOfInstructions, restOpStack, runtimeStack)
                        } else {
                            /*-- else just drop this instruction --*/
                            emulateStackMachine(instructionList.tail, restOpStack, runtimeStack)
                        }
                    }
                    case SkipI(n) => {
                        /* -- drop this instruction and next n -- continue --*/
                        emulateStackMachine(instructionList.drop(n+1), opStack, runtimeStack)
                    }

                    case _ => {
                        /*- Otherwise, just call emulateSingleInstruction -*/
                        val (newOpStack: OpStack, newRuntime:RuntimeStack) = emulateSingleInstruction(opStack, runtimeStack, ins)
                        emulateStackMachine(instructionList.tail, newOpStack, newRuntime)
                    }
                }
            }
        }
}