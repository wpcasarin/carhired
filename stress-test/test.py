from locust import HttpUser, task, between

class MyUser(HttpUser):
    wait_time = between(5, 15)

    @task
    def get_task(self):
        self.client.get("/api/v1/test")

    @task
    def my_post_task(self):
        response = self.client.post("/api/v1/test")
