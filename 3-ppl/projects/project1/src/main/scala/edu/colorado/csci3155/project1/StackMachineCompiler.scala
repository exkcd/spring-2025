package edu.colorado.csci3155.project1

import edu.colorado.csci3155.project1.StackMachineEmulator.emulateSingleInstruction
import edu.colorado.csci3155.project1.StackMachineEmulator.RuntimeStack

object StackMachineCompiler {
    /* Function compileToStackMachineCode
        Given expression e as input, return a corresponding list of stack machine instructions.
        The type of stackmachine instructions are in the file StackMachineEmulator.scala in this same directory
        The type of Expr is in the file Expr.scala in this directory.
     */
    def compileToStackMachineCode(e: Expr): List[StackMachineInstruction] = {
        /* Begin Solution */
        e match {
            /* TODO: Your code here must handle the cases for Expr (see Expr.scala) */
            case Const(f) => List(PushNumI(f))
            case BoolConst(b) => List(PushBoolI(b))
            case Ident(id) => List(StoreEnv(id))
            case Plus(e1, e2) => {
                val v1 = compileToStackMachineCode(e1)
                val v2 = compileToStackMachineCode(e2)
                (v1, v2) match {
                    case (v1, v2) => v1 ++ v2 ++ List(AddI)
                }
            }
            case Minus(e1, e2) => {
                val v1 = compileToStackMachineCode(e1)
                val v2 = compileToStackMachineCode(e2)
                (v1, v2) match {
                    case (v1, v2) => v1 ++ v2 ++ List(SubI)
                }
            }
            case Mult(e1, e2) => {
                val v1 = compileToStackMachineCode(e1)
                val v2 = compileToStackMachineCode(e2)
                (v1, v2) match {
                    case (v1, v2) => v1 ++ v2 ++ List(MultI)
                }
            }
            case Div(e1, e2) => {
                val v1 = compileToStackMachineCode(e1)
                val v2 = compileToStackMachineCode(e2)
                (v1, v2) match {
                    case (v1, v2) => v1 ++ v2 ++ List(DivI)
                }
            }
            case Exp(e1) => {
                val v1 = compileToStackMachineCode(e1)
                v1 match {
                    case v1 => v1 ++ List(ExpI)
                }
            }
            case Log(e) => {
                val v1 = compileToStackMachineCode(e)
                v1 match {
                    case v1 => v1 ++ List(LogI)
                }
            }
            case Sine(e) => {
                val v1 = compileToStackMachineCode(e)
                v1 match {
                    case v1 => v1 ++ List(SinI)
                }
            }
            case Cosine(e) => {
                val v1 = compileToStackMachineCode(e)
                v1 match {
                    case v1 => v1 ++ List(CosI)
                }
            }
            case Geq(e1, e2) => {
                val v1 = compileToStackMachineCode(e1)
                val v2 = compileToStackMachineCode(e2)
                (v1, v2) match {
                    case (v1, v2)=> v1 ++ v2 ++ List(GeqI)
                }
            }
            case Eq(e1, e2) => {
                val v1 = compileToStackMachineCode(e1)
                val v2 = compileToStackMachineCode(e2)
                (v1, v2) match {
                    case (v1, v2) => v1 ++ v2 ++ List(EqI)
                }
            }

            case And(e1, e2) => {
                val v1 = compileToStackMachineCode(e1) // v1 = 3
                val v2 = compileToStackMachineCode(e2)

                v1 ++ List(CSkipI(v1.length+1)) ++ v2 ++ List(SkipI(v1.length+1-v2.length)) ++ List(PushBoolI(false))
            }
            
            case Or(e1, e2) => {
                val v1 = compileToStackMachineCode(e1)
                val v2 = compileToStackMachineCode(e2)

                v1 ++ List(CSkipI(v1.length-1)) ++ List(PushBoolI(true)) ++ List(SkipI(v2.length)) ++ v2
            }

            case Not(e) => {
                val v1 = compileToStackMachineCode(e)
                v1 match {
                    case v1 => v1 ++ List(NotI)
                }
            }

            case IfThenElse(cond, tExpr, elseExpr) => {
                val l0 = compileToStackMachineCode(cond)
                val l1 = compileToStackMachineCode(tExpr)
                val l2 = compileToStackMachineCode(elseExpr)

                l0 ++ List(CSkipI(l1.length+1)) ++ l1 ++ List(SkipI(l2.length)) ++ l2
            }

            case Let(ident, e1, e2) => {
                val v1 = compileToStackMachineCode(e1)
                val v2 = compileToStackMachineCode(e2)
                (v1, v2) match {
                    case (v1, v2) => v1 ++ List(LoadEnv(ident)) ++ v2 ++ List(PopEnv)
                }
            }

            case _ => throw new IllegalArgumentException(s"I do not handle $e")
        }
        /* End Solution */
    }
}
