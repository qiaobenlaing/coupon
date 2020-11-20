//
//  BMSQ_ClerkManageViweController.m
//  BMSQS
//
//  Created by gh on 15/10/31.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "BMSQ_ClerkManageViweController.h"
#import "MJRefresh.h"
#import "BMSQ_ClerkCell.h"

#import "BMSQ_StaffUpdate.h"
@interface BMSQ_ClerkManageViweController() {
    
    UITableView *m_tableView;
    
    UIAlertController *alertC;
    
    NSMutableArray *staffListArray;
    NSInteger pageNumber;
    
    UIView *uploadView;
    UIView * uploadView2;
    
    NSDictionary *m_dic;
    
    UITextField *text_name;
    UITextField *text_tel;
    
    NSDictionary *staffDic;
    
    BOOL isUpdate; //添加还是编辑
    BOOL editHide;//编辑按钮是否隐藏
    
}

@end


@implementation BMSQ_ClerkManageViweController

-(void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    pageNumber = 1;
    [self getClerkAdmin];
    
    
}

- (void)viewDidLoad {
    [super viewDidLoad];

    
    [self setViewUp];
    [self setUploadView];
    
}

- (void)setViewUp {
 
    [self.navigationController setNavigationBarHidden:NO];
    [self.view setBackgroundColor:UICOLOR(242, 242, 242, 1)];
//    [self.navigationItem setTitle:[_shopDic  objectForKey:@"shopName"]];
    [self setNavTitle:[_shopDic  objectForKey:@"shopName"]];
    [self setNavBackItem];

    UIButton* btnRight = [UIButton buttonWithType:UIButtonTypeCustom];
    btnRight.frame = CGRectMake(APP_VIEW_WIDTH - 34, APP_VIEW_ORIGIN_Y-34, 24, 24);
    UIImageView* btnRightView = [[UIImageView alloc]initWithFrame:CGRectMake(0, 0, 24, 24)];
    btnRightView.image = [UIImage imageNamed:@"staff_edit_white"];
    [btnRight addSubview:btnRightView];
    btnRight.tag = 901;
    [btnRight addTarget:self action:@selector(btnAct:) forControlEvents:UIControlEventTouchUpInside];
    
    [self setNavRightBarItem:btnRight];

    
    editHide = YES;
    staffListArray = [[NSMutableArray alloc] init];
    
    
    m_tableView = [[UITableView alloc] initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_CAN_USE_HEIGHT)];
    m_tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    m_tableView.dataSource = self;
    m_tableView.delegate = self;
    m_tableView.backgroundColor = UICOLOR(255, 255, 255, 1);
    [m_tableView addFooterWithTarget:self action:@selector(loadMoreDataSource)];
    [self.view addSubview:m_tableView];

    
    CGFloat x = 20;
    UIButton *button = [[UIButton alloc] initWithFrame:CGRectMake(x, APP_VIEW_HEIGHT-44, APP_VIEW_WIDTH-x*2, 40)];
    button.tag = 1001;
    [button setTitle:@"添加成员" forState:UIControlStateNormal];
    [button setBackgroundColor:UICOLOR(182, 0, 11, 1)];
    [button addTarget:self action:@selector(btnAct:) forControlEvents:UIControlEventTouchUpInside];
    [self.view addSubview:button];
    [self.view bringSubviewToFront:button];
    
    [self setUpAlertController];
    
    
    
    pageNumber = 1;
    [self getClerkAdmin];
    
    
    
    
    
}

- (void)setUpAlertController {
    alertC = [UIAlertController alertControllerWithTitle:@"添加店员" message:nil preferredStyle:UIAlertControllerStyleAlert];
    
    [alertC addTextFieldWithConfigurationHandler:^(UITextField * _Nonnull textField) {
        textField.placeholder = @"请输入员工姓名";
        textField.tag = 3001;
        
    }];
    [alertC addTextFieldWithConfigurationHandler:^(UITextField * _Nonnull textField) {
        textField.placeholder = @"请输入员工手机号";
        textField.tag = 3002;
    }];
    
    [alertC addAction:[UIAlertAction actionWithTitle:@"取消" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        
         NSLog(@"点击取消");
        
    }]];
    [alertC addAction:[UIAlertAction actionWithTitle:@"确定" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        NSLog(@"点击确认");
        if ([alertC.title  isEqual: @"添加店员"]) {
            [self editStaffB];
            
        }else {
            [self updateStaff];
            
        }
        
    }]];
    
    
}

- (void)setUploadView {
    uploadView = [[UIView alloc] init];
    uploadView.frame = self.view.frame;
    uploadView.backgroundColor = [UIColor blackColor];
    uploadView.alpha = 0.3;
    [self.view addSubview:uploadView];
    [self.view bringSubviewToFront:uploadView];
    
    CGFloat x = 10;
    CGFloat y = 15;
    CGFloat height = 25;

    
    uploadView2 = [[UIView alloc] initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH/4*3, 30+3*height)];
    uploadView2.backgroundColor = [UIColor whiteColor];
    uploadView2.center = CGPointMake(APP_VIEW_WIDTH/2, 75);
    [self.view addSubview:uploadView2];
   
    
    CGFloat width = uploadView2.frame.size.width-2*x;
    
    
    text_name = [[UITextField alloc] initWithFrame:CGRectMake(x, y, width, height)];
    text_name.backgroundColor = [UIColor clearColor];
    text_name.placeholder = @"输入店员姓名";
    text_name.font = [UIFont systemFontOfSize:13];
    text_name.layer.cornerRadius=1.0f;
    text_name.layer.masksToBounds=YES;
    text_name.layer.borderColor=[UICOLOR(179, 179, 179, 1) CGColor];
    text_name.layer.borderWidth= 0.3f;
    [uploadView2 addSubview:text_name];
    
    y =y+height+5;
    text_tel = [[UITextField alloc] initWithFrame:CGRectMake(x, y, width, height)];
    text_tel.backgroundColor = [UIColor clearColor];
    text_tel.layer.cornerRadius=1.0f;
    text_tel.layer.masksToBounds=YES;
    text_tel.layer.borderColor=[UICOLOR(179, 179, 179, 1) CGColor];
    text_tel.layer.borderWidth= 0.3f;
    text_tel.placeholder = @"输入手机号码";
    text_tel.font = [UIFont systemFontOfSize:13];
    text_tel.delegate = self;
    text_tel.keyboardType = UIKeyboardTypeNumberPad;
    [uploadView2 addSubview:text_tel];
    
    y = y+height+5;
    UIButton *btn_aff = [[UIButton alloc] initWithFrame:CGRectMake(x+5, y, width/2-10, height)];
    btn_aff.backgroundColor = UICOLOR(197, 8, 9, 1);
    btn_aff.tag = 2001;
    [btn_aff setTitle:@"确定" forState:UIControlStateNormal];
    [btn_aff addTarget:self action:@selector(btnAct:) forControlEvents:UIControlEventTouchUpInside];
    [uploadView2 addSubview:btn_aff];
    

    UIButton *btn_cancel = [[UIButton alloc] initWithFrame:CGRectMake(x+width/2+5, y, width/2-10, height)];
    btn_cancel.backgroundColor = UICOLOR(255, 255, 255, 1);
    btn_cancel.tag = 2002;
    [btn_cancel setTitle:@"取消" forState:UIControlStateNormal];
    [btn_cancel setTitleColor:UICOLOR(77, 77, 77, 1) forState:UIControlStateNormal];
    btn_cancel.layer.cornerRadius=1.0f;
    btn_cancel.layer.masksToBounds=YES;
    btn_cancel.layer.borderColor=[UICOLOR(179, 179, 179, 1) CGColor];
    btn_cancel.layer.borderWidth= 0.3f;
    [btn_cancel addTarget:self action:@selector(btnAct:) forControlEvents:UIControlEventTouchUpInside];
    [uploadView2 addSubview:btn_cancel];
    
    
    uploadView.hidden = YES;
    uploadView2.hidden = YES;
    
}



- (void)btnAct:(UIButton *)sender {
    
    if (sender.tag == 1001) {
        
        [self presentViewController:alertC animated:YES completion:^{
            alertC.title = @"添加店员";
            UITextField *textField1 = [alertC.textFields objectAtIndex:0];
            UITextField *textField2 = [alertC.textFields objectAtIndex:1];
            textField1.text = @"";
            textField2.text = @"";
            
            
        }];
        
    
    }else if (sender.tag == 2001) {//确定

        if (isUpdate) {
            [self updateStaff];
            
        }else {
            [self editStaffB];
            
        }
        
    }else if (sender.tag == 901) {//编辑按钮
        editHide = !editHide;
        [m_tableView reloadData];
        
    }
}


#pragma mark - UITableView Delegate
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 4;
}


- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    if(section == 3) {
        return staffListArray.count;
    }
    
    return 1;
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    if (indexPath.section == 0||indexPath.section == 2) {
        NSString *identify = @"clerkHeadCell";
        
        NSString *cellString;
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identify];
        if (cell == nil) {
            cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identify];
            
        }

        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        cell.backgroundColor = [UIColor whiteColor];
        UIView *line = [[UIView alloc] initWithFrame:CGRectMake(0, 43.5, APP_VIEW_WIDTH, 0.5)];
        line.backgroundColor = APP_CELL_LINE_COLOR;
        [cell addSubview:line];
        if (indexPath.section == 0) {
            cellString = @"店长";
            
        } else if (indexPath.section == 2) {
            cellString = @"店员管理";
            
        }
        
        UILabel *label = [[UILabel alloc] initWithFrame:CGRectMake(10, 0, APP_VIEW_WIDTH-20, 44)];
        label.font = [UIFont systemFontOfSize:15];
        label.text = cellString;
        [cell addSubview:label];
        
        return cell;
    }
    if (indexPath.section == 1) {
        UITableViewCell *cell = [[UITableViewCell alloc] init];
        
        cell.backgroundColor = [UIColor whiteColor];
        if([m_dic count]>0){
            cell.textLabel.text = [NSString stringWithFormat:@"%@: %@",[m_dic objectForKey:@"realName"], [m_dic objectForKey:@"mobileNbr"]];
            
        }
        cell.textLabel.font = [UIFont systemFontOfSize:15];
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        
        UIView *line = [[UIView alloc] initWithFrame:CGRectMake(0, 44, APP_VIEW_WIDTH, 10)];
        line.backgroundColor = UICOLOR(242, 242, 242, 1);
        [cell addSubview:line];
        
        return cell;

    }else if (indexPath.section == 3) {
        NSString *cellIdentify = @"BMSQ_ClerkCell";
        
        BMSQ_ClerkCell *cell = (BMSQ_ClerkCell*)[tableView dequeueReusableCellWithIdentifier:cellIdentify];
        if (cell == nil) {
            cell = [[BMSQ_ClerkCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIdentify];
            
        }
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        cell.delegate = self;
        
        
        
        if ([[staffListArray objectAtIndex:indexPath.row] count]>0) {
            [cell setCellValue:[staffListArray objectAtIndex:indexPath.row]];
            
        }
        
        
        if (editHide) {
            cell.btnEdit.hidden = YES;
            
        }else if (!editHide) {
            cell.btnEdit.hidden = NO;
            
        }
        
        
        return cell;
        
    }
    
    
    return nil;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    

    
}


- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    if (indexPath.section == 1) {
        return 54;
        
    }
    
    return 44;
}


/**
 *  请求更多数据
 */
-(void)loadMoreDataSource{
    pageNumber++;
    
    /**
     *  请求数据
     */
    [self getClerkAdmin];
}


#pragma mark - http request

- (void)delStaff {
    NSLog(@"删除");
    
    [SVProgressHUD showWithStatus:@""];
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[staffDic objectForKey:@"staffCode"] forKey:@"staffCode"];
    
    NSString* vcode = [gloabFunction getSign:@"delStaff" strParams:[staffDic objectForKey:@"staffCode"]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    __weak typeof(self) weakSelf = self;
    [self.jsonPrcClient invokeMethod:@"delStaff" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        [SVProgressHUD dismiss];
        
        NSLog(@"%@",responseObject);
        NSString *code =  [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"code"]];
        if (code.intValue == 50000){
            pageNumber = 1;
            [self getClerkAdmin];
            
        }else if (code.intValue == 20000) {
            CSAlert(@"失败，请重试");
            
        }
        
        
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];
        CSAlert(@"失败，请重试");
    }];
    
    
    
    
    
    
}

- (void)updateStaff {//修改员工
    
    NSString *tel = [alertC.textFields objectAtIndex:1].text;
    NSString *name = [alertC.textFields objectAtIndex:0].text;
    
    if ([tel  isEqual: @""] || [name  isEqual: @""]) {
        CSAlert(@"请将信息填写完整");
        return;
    }
    if (tel.length != 11) {
        CSAlert(@"请填写正确的手机号");
        return;
    }
    
    [SVProgressHUD showWithStatus:@""];
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[staffDic objectForKey:@"staffCode"] forKey:@"staffCode"];
    [params setObject:tel forKey:@"mobileNbr"];
    [params setObject:name forKey:@"realName"];
    [params setObject:@"1" forKey:@"userLvl"];
    [params setObject:[gloabFunction getShopCode] forKey:@"shopCode"];
    [params setObject:[gloabFunction getStaffCode] forKey:@"parentCode"];
    
    NSString* vcode = [gloabFunction getSign:@"editStaffB" strParams:[staffDic objectForKey:@"staffCode"]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    [self.jsonPrcClient invokeMethod:@"editStaffB" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        [SVProgressHUD dismiss];
        NSString *code =  [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"code"]];
        if (code.intValue == 50000){

            [self.view endEditing:YES];
            
            pageNumber = 1;
            [self getClerkAdmin];

        }else if (code.intValue == 20000) {
            CSAlert(@"失败，请重试");
            
        }else if (code.intValue == 60000) {
            CSAlert(@"失败，请重试");
            
        }else if (code.intValue == 60001) {
            CSAlert(@"请检查手机号码");
            
        }else if (code.intValue == 20000) {
            CSAlert(@"手机号码不正确");
            
        }else if (code.intValue == 80040) {
            CSAlert(@"请输入员工姓名");
            
        }else if (code.intValue == 80041) {
            CSAlert(@"请输入员工类型");
            
        }else if (code.intValue == 80043) {
            CSAlert(@"该手机号码已被使用");
        }
        else {
             CSAlert(@"未知的错误");
            
        }
        NSLog(@"%@",responseObject);
        
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];
        
    }];
    
    
    
    
}


- (void)editStaffB {//添加员工
    NSString *tel = [alertC.textFields objectAtIndex:1].text;
    NSString *name = [alertC.textFields objectAtIndex:0].text;
    
    if ([tel  isEqual: @""] || [name  isEqual: @""]) {
        CSAlert(@"请将信息填写完整");
        return;
    }
    if (tel.length != 11) {
        CSAlert(@"请填写正确的手机号");
        return;
    }
    
    
    [SVProgressHUD showWithStatus:@""];
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    
    [params setObject:@"" forKey:@"staffCode"];
    [params setObject:tel forKey:@"mobileNbr"];
    [params setObject:name forKey:@"realName"];
    [params setObject:@"1" forKey:@"userLvl"];
    [params setObject:[_shopDic objectForKey:@"shopCode"] forKey:@"shopCode"];
    [params setObject:[gloabFunction getStaffCode] forKey:@"parentCode"];
    
    NSString* vcode = [gloabFunction getSign:@"editStaffB" strParams:@""];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    [self.jsonPrcClient invokeMethod:@"editStaffB" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        [SVProgressHUD dismiss];
        
        NSString *code =  [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"code"]];
        if (code.intValue == 50000){
            pageNumber = 1;
            [self getClerkAdmin];

        }else if (code.intValue == 20000) {
            CSAlert(@"失败，请重试");
            
        }else if (code.intValue == 60000) {
            CSAlert(@"失败，请重试");
            
        }else if (code.intValue == 60001) {
            CSAlert(@"请输入手机号码");
            
        }else if (code.intValue == 20000) {
            CSAlert(@"手机号码不正确");
            
        }else if (code.intValue == 80040) {
            CSAlert(@"请输入员工姓名");
            
        }else if (code.intValue == 80041) {
            CSAlert(@"请输入员工类型");
            
        }else if (code.intValue == 80043) {
            CSAlert(@"该手机号码已被使用");
        }
        else {
            CSAlert(@"未知的错误");
            
        }
        
        
        NSLog(@"%@",responseObject);
        
        
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        CSAlert(@"错误");
        [SVProgressHUD dismiss];
        
    }];
    
}


- (void)getClerkAdmin {
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    
    [params setObject:[_shopDic objectForKey:@"shopCode"] forKey:@"shopCode"];
    [params setObject:[NSString stringWithFormat:@"%ld",(long)pageNumber] forKey:@"page"];
    
    NSString* vcode = [gloabFunction getSign:@"getClerkAdmin" strParams:[_shopDic objectForKey:@"shopCode"]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    [self.jsonPrcClient invokeMethod:@"getClerkAdmin" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        [m_tableView footerEndRefreshing];
        
        NSLog(@"%@",responseObject);
        if (pageNumber == 1) {
            [staffListArray removeAllObjects];
            
        }
        m_dic = [responseObject objectForKey:@"managerInfo"];
        if ([[responseObject objectForKey:@"staffList"] count] == 0 ) {
            [m_tableView removeFooter];
        }
        [staffListArray addObjectsFromArray:[responseObject objectForKey:@"staffList"]];
        
        [m_tableView reloadData];
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        
        
    }];
    
    
    
}


#pragma mark - UITextFiled delegate
- (BOOL)textField:(UITextField *)textField shouldChangeCharactersInRange:(NSRange)range replacementString:(NSString *)string{
    
    NSString * toBeString = [textField.text stringByReplacingCharactersInRange:range withString:string];
    
    if (toBeString.length > 11 && range.length!=1){
        textField.text = [toBeString substringToIndex:11];
        return NO;
        
    }
    return YES;
}

-(void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event{
    [self.view endEditing:YES];
    
}

#pragma mark - ClerkCell Delegate
- (void)cellBtnClick:(id)object {//编辑
    staffDic = object;
    [self presentViewController:alertC animated:YES completion:^{
        alertC.title = @"编辑店员";
        UITextField *textField1 = [alertC.textFields objectAtIndex:0];
        UITextField *textField2 = [alertC.textFields objectAtIndex:1];
        textField1.text = [object objectForKey:@"realName"];
        textField2.text = [object objectForKey:@"mobileNbr"];
        
    }];
    
    
    
}

- (void)cellDelStaff:(id)object { //删除
    
    staffDic = object;
    [self delStaff];

}

@end
