import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class expressionCalc {
    public static void main(String[] args) {
        iniciar();
    }

    public static void iniciar() {
        String expression = "";
        System.out.println("Digite a sua expressão: ");

        Scanner sc = new Scanner(System.in);
        while(sc.hasNext()){
            expression = sc.next();
            System.out.println("=============================");
            System.out.println("Resulado = " + calSumSub(expression));
            System.out.println("=============================");
        }

        sc.close(); 
        

    }

    public static float calSumSub(String expression) {

        if(expression.contains("{") || expression.contains("[")) {
            expression = expression.replace("{", "(");
            expression = expression.replace("}", ")");
            expression = expression.replace("[", "(");
            expression = expression.replace("]", ")");
        }


        if(expression.contains("sqrt")){
            Pattern pattern = Pattern.compile("[\\-]?sqrt\\(.*\\)");
            Matcher matcher = pattern.matcher(expression);
            if(matcher.find()) {
                String auxExpression = matcher.group();
                String auxExpression2 = auxExpression.replace("sqrt", "");
                auxExpression2 = Float.toString(calSumSub(auxExpression2)) + "^(1/2)";
                expression = expression.replace(auxExpression, auxExpression2);
            }
        }

        if(expression.contains("(")){
            Pattern pattern = Pattern.compile("\\(.*\\)");
            Matcher matcher = pattern.matcher(expression);
            if(matcher.find()) {
                String auxExpression = matcher.group();
                float f = calSumSub(removeParenteses(auxExpression));
                expression = expression.replace(auxExpression, Float.toString(f));
            }
        }

        if(expression.contains("^")) {
            expression = calDivMult(expression, "[\\-]?[0-9]*[\\.]?[0-9]*\\^(\\-)?[0-9]+[\\.]?[0-9]*", "expo");
        }

        if(expression.contains("/")) {
            expression = calDivMult(expression, "[\\-]?[0-9]*[\\.]?[0-9]*\\/(\\-)?[0-9]+[\\.]?[0-9]*", "div");
        }
        if(expression.contains("*")) {
            expression = calDivMult(expression, "[\\-]?[0-9]+[\\.]?[0-9]*(\\*)(\\-)?[0-9]+[\\.]?[0-9]*", "mult");
        }

        
        float result = 0;
        String partExpressao = "";
        Pattern pattern = Pattern.compile("(\\+|\\-)?[0-9]+[\\.]?[0-9]*");
        Matcher matcher = pattern.matcher(expression);
        while(matcher.find()) {
            if (matcher.group().contains("-")) {
                partExpressao = matcher.group().replace("-", "");
                result = result - Float.parseFloat(partExpressao);
            }else {
                partExpressao = matcher.group().replace("+", "");
                result = result + Float.parseFloat(partExpressao);
            }
            
        }
        

        return result;
        
    }

    public static String calDivMult(String expression, String regex, String operacao) {
        Boolean ocorrencia = true;
        float result = 0;
        while(ocorrencia) {
            ocorrencia = false;
            String partExpressao = "";
            Float parteA, parteB;
            Pattern pattern = Pattern.compile(regex); 
            Matcher matcher = pattern.matcher(expression);
            if(matcher.find()) {
                
                partExpressao = matcher.group();
                
                if (operacao == "expo") {
                    String partes[] = partExpressao.split("\\^");
                    parteA = Float.parseFloat(partes[0]);
                    parteB = Float.parseFloat(partes[1]);
                    if(parteA < 0) {

                        if(denominadorPar(parteB)) {
                            System.out.println("Em breve: números imaginários");
                        }else {
                            parteA = parteA * - 1;
                            Double dAux;
                            dAux = (Double) Math.pow(parteA, parteB);
                            result = dAux.floatValue() * -1;
                        }
                    }else {
                        Double dAux;
                        dAux = (Double) Math.pow(parteA, parteB);
                        result = dAux.floatValue();
                    }
                } else if(operacao == "div") {
                    String partes[] = partExpressao.split("\\/");
                    parteA = Float.parseFloat(partes[0]);
                    parteB = Float.parseFloat(partes[1]);
                    result = parteA / parteB;
                } else if (operacao == "mult") {
                    String partes[] = partExpressao.split("\\*");
                    parteA = Float.parseFloat(partes[0]);
                    parteB = Float.parseFloat(partes[1]);
                    result = parteA * parteB;
                }
                expression = expression.replace(partExpressao, Float.toString(result));
                
                ocorrencia = true;
            }

        }
        return expression;
    }


    public static String removeParenteses(String s) {
        s = s.replaceFirst("\\(", "");
        char [] sVetor = s.toCharArray();
        String sOutput = "";
        for (int i = 0; i < sVetor.length - 1; i++) {
            sOutput = sOutput + sVetor[i];
        }
        return sOutput;

    }

    
    public static String removeCharInicial(String s) {
        char [] sVetor = s.toCharArray();
        String sOutput = "";
        for (int i = 1; i < sVetor.length; i++) {
          sOutput = sOutput + sVetor[i];
        }
    
        return sOutput;
    
      }

      public static boolean seRepete(String regex, String expression) {
        boolean seRepete = false;
        Pattern pattern = Pattern.compile(regex); 
        Matcher matcher = pattern.matcher(expression);
        
        matcher.find();
        String sinicial = matcher.group(); 
        matcher.find();
        String sfinal = matcher.group(); 

        if(sinicial.equals(sfinal)) {
            seRepete = true;
        }
       
        return seRepete;
      }
    
    public static boolean isDizima(float f){
        String s = Float.toString(f);
        s = removeCharInicial(s);
        s = removeCharInicial(s);
        String regex = "[0-9][0-9][0-9]";
        if (s.length() < 7){
            return false;
        }else if (seRepete(regex, s)) {
            return true;
        }else {
            return false;
        }
        
      }
    

    public static String decimalToFraction(float f) {
        String newFraction = "";
        String expression = Float.toString(f);
        expression = removeCharInicial(expression);
        expression = removeCharInicial(expression);
        Pattern pattern = Pattern.compile("[0-9][0-9][0-9]"); 
        Matcher matcher = pattern.matcher(expression);

        String numerador;
        String denominador;
        if (isDizima(f)) {
            
            matcher.find();
            numerador = matcher.group();
            denominador = "999";
            newFraction = numerador + "/" + denominador;

            
        }else {
            
            numerador = Float.toString(f);
            numerador = numerador.replace(".", "").replace("0", "");
            char[] sVetor = numerador.toCharArray();
            denominador = "1";
            for (int i = 0; i < sVetor.length; i++) {
                denominador = denominador + "0";
            }
            newFraction = numerador + "/" + denominador;
        }
        return simplificaFraction(newFraction);

    }
    
    public static String simplificaFraction(String fracao) {
        String[] partes = fracao.split("\\/");
        int numerador = Integer.parseInt(partes[0]);
        int denominador = Integer.parseInt(partes[1]);
        int mdc = mdc(numerador, denominador);
        
        numerador = numerador / mdc;
        denominador = denominador / mdc;
    
        String sFraction = numerador + "/" + denominador;
        return sFraction;
    }

    public static int mdc(int a, int b) {
        if (a < b) {
            int aux = a;
            a = b;
            b = aux;
        }

        int mdc = 1;
        int cont = 2;
        while(cont <= b) {
        
        if (a % cont == 0 && b % cont == 0) {
        a = a / cont;
        b = b / cont;
        mdc = mdc * cont;
        if (cont == a){
            return mdc;
        }
        
        } else {
            cont++;
        }
        
        }
    
        return mdc;
    }

    public static boolean denominadorPar(Float f) {
        String s = decimalToFraction(f);
        s = removeCharInicial(s);
        s = removeCharInicial(s);
        int i = Integer.parseInt(s);
        if (i % 2 == 0) {
            return true;

        }else {
            return false;

        }
    }

}

// log10 = 10^x = 10
// 4
// 2/2*3
