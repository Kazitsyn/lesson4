package ru.geekbrains.junior.lesson4.homework.hibernat;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.geekbrains.junior.lesson4.models.Course;
import ru.geekbrains.junior.lesson4.models.Student;

public class App {
    public static void main(String[] args) {
        // Создание фабрики сессий
        SessionFactory sessionFactory = new Configuration()
                .configure("hibernatehw.cfg.xml")
                .addAnnotatedClass(Course.class)
                .buildSessionFactory();

        // Создание сессии
        Session session = sessionFactory.getCurrentSession();

        try {
            // Начало транзакции
            session.beginTransaction();

            // Создание объекта
            Course course = Course.create();

            session.save(course);
            System.out.println("Object student save successfully");

            Course retrievedCourse = session.get(Course.class, course.getId());
            System.out.println("Object student retrieved successfully");
            System.out.println("Retrieved student object: " + retrievedCourse);

            // Обновление объекта
            retrievedCourse.updateTitle();
            retrievedCourse.updateDuration();
            session.update(retrievedCourse);
            System.out.println("Object student update successfully");

            // Удаление объекта
            //session.delete(retrievedCourse);
            //System.out.println("Object student delete successfully");

            // Коммит транзакции
            session.getTransaction().commit();
        }
        finally {
            // Закрытие фабрики сессий
            sessionFactory.close();
        }
    }
}
