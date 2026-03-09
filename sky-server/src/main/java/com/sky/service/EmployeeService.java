package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    /**
     * 新增员工
     * @param employeeDTO
     */
    void addEmployee(EmployeeDTO employeeDTO);

    /**
     * 员工分页查询
     * @param employeePageQueryDTO
     * @return
     */
    PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 更新员工启用禁用状态
     * @param status
     * @param id
     */
    void updateEmployeeStatus(Integer status, Long id);

    /**
     * 编辑员工
     * @param employeeDTO
     */
    void updateEmployee(EmployeeDTO employeeDTO);

    /**
     * 根据id查询员工信息
     * @param id
     * @return
     */
    Employee getById(Long id);
}
