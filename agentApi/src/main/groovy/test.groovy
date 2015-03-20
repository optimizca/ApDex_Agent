import org.quartz.CronExpression

c = new CronExpression("0 */15 * ? * *");

Date d = c.getNextValidTimeAfter(new Date());
int i = 0;
for (;i < 20; i++) {
    println d;
    d = c.getNextValidTimeAfter(d);
}

