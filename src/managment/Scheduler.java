package managment;

import clinic.Doctor;
import utilities.Timer;

import javax.print.Doc;
import java.sql.Time;
import java.util.Iterator;

public class Scheduler implements Runnable {

    @Override
    public void run() {
        while (Timer.getCurranTime() < 240) {
            while (Department.patientQueue.size() > 0) {
                boolean patientAttended = false;

                Iterator doctorsIterator = Department.doctorsHeap.iterator();
                while (doctorsIterator.hasNext()) {
                    Doctor currentDoctor = (Doctor) doctorsIterator.next();

                    if (currentDoctor.isAvailable()) {
                        if (currentDoctor.getTreatedPatients() - Department.doctorsHeap.peek().getTreatedPatients() < 3) {
                            currentDoctor.getClinic().insertPateint(Department.patientQueue.remove());
                            currentDoctor.notify();
                            patientAttended = true;
                            break;
                        }
                    }
                }

                if (!patientAttended) {
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                    }
                }

            }
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
