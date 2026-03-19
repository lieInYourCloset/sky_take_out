package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        // TODO 后期需要进行md5加密，然后再进行比对 DONE!
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }

    /**
     * 新增员工
     * @param employeeDTO
     */
    @Override
    public void addEmployee(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();

        //对象属性拷贝
        BeanUtils.copyProperties(employeeDTO, employee);

        //设置账户状态为启用状态
        employee.setStatus(StatusConstant.ENABLE);

        //设置初始密码，进行md5加密
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));

//        //设置创建人和修改人
//        employee.setCreateUser(BaseContext.getCurrentId());
//        employee.setUpdateUser(BaseContext.getCurrentId());
//
//        //设置创建时间和修改时间
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());

        //保存员工信息到数据库
        Long id = employeeMapper.insert(employee);
        employee.setId(id);

    }

    /**
     * 员工分页查询
     * @param employeePageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
        // 1. 使用 try-with-resources 确保 Page 对象自动关闭（清理 ThreadLocal）
        try (Page<Employee> page = PageHelper.startPage(employeePageQueryDTO.getPage(), employeePageQueryDTO.getPageSize())) {

            // 2. 执行查询：MyBatis 拦截器会处理分页逻辑
            // 注意：此处不需要再次赋值给 List，直接从 page 对象获取即可，因为 page 本身就是一个 ArrayList<> 的子类
            employeeMapper.pageQuery(employeePageQueryDTO);

            // 3. 封装并返回结果
            // page.getTotal() 获取总记录数，page.getResult() 获取当前页数据列表
            return new PageResult(page.getTotal(), page.getResult());
        }
        // try 块结束时，page.close() 会被自动调用，内部执行 PageHelper.clearPage()
    }

    /**
     * 更新员工启用禁用状态
     * @param status
     * @param id
     */
    @Override
    public void updateEmployeeStatus(Integer status, Long id) {
        Employee employee = new Employee();
        employee.setId(id);
        employee.setStatus(status);
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser(BaseContext.getCurrentId());
        employeeMapper.updateEmployeeStatus(employee);
    }

    /**
     * 编辑员工
     * @param employeeDTO
     */
    @Override
    public void updateEmployee(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        //对象属性拷贝
        BeanUtils.copyProperties(employeeDTO, employee);
//        //设置修改人和修改时间
//        employee.setUpdateUser(BaseContext.getCurrentId());
//        employee.setUpdateTime(LocalDateTime.now());
        employeeMapper.updateEmployee(employee);
    }

    /**
     * 根据id查询员工信息
     * @param id
     * @return
     */
    @Override
    public Employee getById(Long id) {
        return employeeMapper.getById(id);
    }

}
