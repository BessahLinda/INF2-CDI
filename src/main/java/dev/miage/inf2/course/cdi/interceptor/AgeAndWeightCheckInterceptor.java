package dev.miage.inf2.course.cdi.interceptor;

import dev.miage.inf2.course.cdi.exception.SaleNotPermittedException;
import dev.miage.inf2.course.cdi.model.Candy;
import dev.miage.inf2.course.cdi.model.Customer;
import jakarta.annotation.Priority;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;



@Interceptor
@Priority(Interceptor.Priority.APPLICATION)
@AgeAndWeightCheck
public class AgeAndWeightCheckInterceptor {

    @AroundInvoke
    public Object checkAgeAndWeight(InvocationContext ctx) throws Exception {
        Object[] parameters = ctx.getParameters();

        if (parameters[0] instanceof Customer && parameters[1] instanceof Candy) {
            Customer customer = (Customer) parameters[0];
            Candy candy = (Candy) parameters[1];

            if (customer.age() < 3 && candy.getWeight() > 10) {
                throw new SaleNotPermittedException("Vente interdite: Trop de bonbon pour un enfant de moins de 3 ans.");
            }
        }

        return ctx.proceed();
    }
}
