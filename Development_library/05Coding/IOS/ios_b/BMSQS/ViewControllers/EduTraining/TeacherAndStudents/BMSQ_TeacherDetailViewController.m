//
//  BMSQ_TeacherDetailViewController.m
//  BMSQS
//
//  Created by 新利软件－冯 on 16/3/3.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "BMSQ_TeacherDetailViewController.h"
#import "HonorWallViewCell.h"
#import "SVProgressHUD.h"
#import "UIImageView+WebCache.h"
#import "BMSQ_AddStarViewController.h"
#import "PhotoScrollView.h"
#import "BMSQ_StarTeacherViewController.h"
#import "BMSQ_AddStudentViewController.h"
@interface BMSQ_TeacherDetailViewController ()<UITableViewDataSource, UITableViewDelegate, PhotoScrollDelegate>

@property (nonatomic, strong) UITableView * tableView;
@property (nonatomic, strong) NSDictionary * dataSource;

@end

@implementation BMSQ_TeacherDetailViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.view.backgroundColor = APP_VIEW_BACKCOLOR;
    [self setNavBackItem];
    [self customRightBtn];
    if (self.number == 1) {
        
        [self setNavTitle:self.nameStr];
        [self getShopTeacherInfo];
        
    }else if (self.number == 2){
        [self setNavTitle:@"学员之星"];
        [self getNewestStudentStar];
    }
    
    NSNotificationCenter * defaultCenter1 = [NSNotificationCenter defaultCenter];
    [defaultCenter1 addObserver:self
                       selector:@selector(teacherDetailListNot:)
                           name:@"teacherDetailList"
                         object:nil];
    
//    [self setViewUp];
    
}

- (void)customRightBtn
{
    UIButton * deleteItem = [UIButton buttonWithType:UIButtonTypeCustom];
    deleteItem.frame = CGRectMake(APP_VIEW_WIDTH - 85, 20, 44, 44);
    [deleteItem setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    deleteItem.titleLabel.font = [UIFont systemFontOfSize:14.0];
    if (self.number == 1) {
        [deleteItem setTitle:@"删除" forState:UIControlStateNormal];
    }else if (self.number == 2){
        [deleteItem setTitle:@"新星" forState:UIControlStateNormal];
    }
    [deleteItem addTarget:self action:@selector(deleteClick:) forControlEvents:UIControlEventTouchUpInside];
    
    
    UIButton * amendItem = [UIButton buttonWithType:UIButtonTypeCustom];
    amendItem.frame = CGRectMake(APP_VIEW_WIDTH - 50, 20, 44, 44);
    amendItem.titleLabel.font = [UIFont systemFontOfSize:14.0];
    [amendItem setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    if (self.number == 1) {
        [amendItem setTitle:@"修改" forState:UIControlStateNormal];
    }else if (self.number == 2){
        [amendItem setTitle:@"编辑" forState:UIControlStateNormal];
    }
    [amendItem addTarget:self action:@selector(amendClick:) forControlEvents:UIControlEventTouchUpInside];
    
    [self setNavRightBarItem:deleteItem];
    [self setNavRightBarItem:amendItem];
    
}

- (void)setViewUp
{
    UIView * topView = [[UIView alloc] initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_HEIGHT/3 + 40)];
    topView.backgroundColor = [UIColor whiteColor];
    [self.view addSubview:topView];
    
    UIImageView * teacherIcon = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH,  APP_VIEW_HEIGHT/3)];
    if (self.number == 1) {
        [teacherIcon sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", APP_SERVERCE_IMG_URL,self.dataSource[@"teacherImgUrl"] ]] placeholderImage:[UIImage imageNamed:@"iv_noShopLog"]];
    }else if (self.number == 2){
        [teacherIcon sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", APP_SERVERCE_IMG_URL,self.dataSource[@"starUrl"] ]] placeholderImage:[UIImage imageNamed:@"iv_noShopLog"]];
    }
    
    [topView addSubview:teacherIcon];
    
    UILabel * teacherName = [[UILabel alloc] initWithFrame:CGRectMake(0, APP_VIEW_HEIGHT/3 + 5, APP_VIEW_WIDTH/3, 30)];
//    teacherName.backgroundColor = [UIColor purpleColor];
    if (self.number == 1) {
        teacherName.text = [NSString stringWithFormat:@"%@", self.dataSource[@"teacherName"]];
    }else if (self.number == 2){
        teacherName.text = [NSString stringWithFormat:@"%@", self.dataSource[@"studentName"]];
    }
    
    teacherName.font = [UIFont systemFontOfSize:14.0];
    teacherName.textAlignment = NSTextAlignmentCenter;
    [topView addSubview:teacherName];
    
    UILabel * teacherLevel = [[UILabel alloc] initWithFrame:CGRectMake(APP_VIEW_WIDTH/3, APP_VIEW_HEIGHT/3 + 5, APP_VIEW_WIDTH/3, 30)];
//    teacherLevel.backgroundColor = [UIColor redColor];
    if (self.number == 1) {
        teacherLevel.text = [NSString stringWithFormat:@"%@", self.dataSource[@"teacherTitle"]];
    }else if (self.number == 2){
        teacherLevel.text = [NSString stringWithFormat:@"%@", self.dataSource[@"className"]];
    }
    
    teacherLevel.font = [UIFont systemFontOfSize:14.0];
    teacherLevel.textAlignment = NSTextAlignmentCenter;
    [topView addSubview:teacherLevel];
    
    UILabel * teacherSubject = [[UILabel alloc] initWithFrame:CGRectMake(APP_VIEW_WIDTH/3*2, APP_VIEW_HEIGHT/3 + 5, APP_VIEW_WIDTH/3, 30)];
//    teacherSubject.backgroundColor = [UIColor yellowColor];
    if (self.number == 1) {
       teacherSubject.text = [NSString stringWithFormat:@"%@", self.dataSource[@"teachCourse"]];
    }else if (self.number == 2){
        teacherSubject.text = [NSString stringWithFormat:@"%@岁", self.dataSource[@"starAge"]];
    }
    
    teacherSubject.font = [UIFont systemFontOfSize:14.0];
    teacherSubject.textAlignment = NSTextAlignmentCenter;
    [topView addSubview:teacherSubject];
    
    self.tableView = [[UITableView alloc] initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y + APP_VIEW_HEIGHT/3 + 40, APP_VIEW_WIDTH, APP_VIEW_HEIGHT/2 - 15) style:UITableViewStyleGrouped];
    _tableView.dataSource = self;
    _tableView.delegate = self;
    
    [self.view addSubview:self.tableView];
}

#pragma mark ------ UITableViewDataSource, UITableViewDelegate
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 2;
}
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if (section == 0) {
        return 1;
    }else{
        
        if (self.number == 1) {
            NSArray * array = [NSArray arrayWithArray:self.dataSource[@"teacherWork"]];
            return array.count;
        }else{
            NSArray * array = [NSArray arrayWithArray:self.dataSource[@"starWork"]];
            return array.count;
        }
        
    }
}
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (indexPath.section == 0) {
        
        static NSString *idenftier = @"shopInfoCellsd";
       UITableViewCell * cell = [tableView dequeueReusableCellWithIdentifier:idenftier];
        if (cell == nil) {
            cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:idenftier];
            UILabel *labelTitle = [[UILabel alloc]initWithFrame:CGRectMake(12, 0, 100, 30)];
            labelTitle.text = @"简介:";
            labelTitle.font = [UIFont systemFontOfSize:14];
            [cell addSubview:labelTitle];
            
            
            UILabel *desLabel = [[UILabel alloc]initWithFrame:CGRectMake(10, 30, APP_VIEW_WIDTH-20, 0)];
            desLabel.textColor = APP_TEXTCOLOR;
            desLabel.font = [UIFont systemFontOfSize:13];
            desLabel.numberOfLines = 0;
            desLabel.tag = 100;
            [cell addSubview:desLabel];
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
        }
        
        UILabel *deslabel = (UILabel *)[cell viewWithTag:100];
        if (self.number == 1) {
            deslabel.text = self.dataSource[@"teacherInfo"];
        }else if (self.number == 2){
            deslabel.text = self.dataSource[@"starInfo"];
        }
        
        CGSize size = [deslabel.text boundingRectWithSize:CGSizeMake(APP_VIEW_WIDTH-20, MAXFLOAT) options:NSStringDrawingTruncatesLastVisibleLine | NSStringDrawingUsesFontLeading | NSStringDrawingUsesLineFragmentOrigin attributes:@{NSFontAttributeName: deslabel.font} context:nil].size;
        deslabel.frame = CGRectMake(10, 30, size.width, size.height);
        
        return cell;
        
    }else{
        static NSString * cell_id = @"HonorWallViewCellss";
        HonorWallViewCell * cell = [tableView dequeueReusableCellWithIdentifier:cell_id];
        if (!cell) {
            cell = [[HonorWallViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cell_id];
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
        }
        
        [cell setCellHonorDic:self.dataSource[@"teacherWork"][indexPath.row] number:self.number];
        
        return cell;
    }
    
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (indexPath.section == 0) {
        if (self.number == 1) {
            NSString *str = self.dataSource[@"teacherInfo"];
            CGSize size = [str boundingRectWithSize:CGSizeMake(APP_VIEW_WIDTH-20, MAXFLOAT) options:NSStringDrawingTruncatesLastVisibleLine | NSStringDrawingUsesFontLeading | NSStringDrawingUsesLineFragmentOrigin attributes:@{NSFontAttributeName: [UIFont systemFontOfSize:13.f]} context:nil].size;
            return  40+size.height;
        }else{
            NSString *str = self.dataSource[@"starInfo"];
            CGSize size = [str boundingRectWithSize:CGSizeMake(APP_VIEW_WIDTH-20, MAXFLOAT) options:NSStringDrawingTruncatesLastVisibleLine | NSStringDrawingUsesFontLeading | NSStringDrawingUsesLineFragmentOrigin attributes:@{NSFontAttributeName: [UIFont systemFontOfSize:13.f]} context:nil].size;
            return  40+size.height;
        }
        
    }else{

        return 185;
        
    }
}


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (indexPath.section == 1) {
        
//        NSLog(@"放大照片");
        PhotoScrollView *sc = [[PhotoScrollView alloc]init];
        UIApplication *app = [UIApplication sharedApplication];
        UIWindow *window = app.keyWindow;
        __block PhotoScrollView *_weakPhoto=sc;
        sc.removeSC = ^{
            [_weakPhoto removeFromSuperview];
            
        };
        sc.delegate = self;
        
        if (self.number == 1){ // 看名师堂
            NSArray * array = [NSArray arrayWithArray:self.dataSource[@"teacherWork"]];
            sc.count = (int)array.count;
            [sc setHononrImageArray:array string:@"workUrl"];
        }else if (self.number == 2){ // 看今日之星
            NSArray * array = [NSArray arrayWithArray:self.dataSource[@"starWork"]];
            sc.count = (int)array.count;
            [sc setHononrImageArray:array string:@"starImgUrl"];
        }
        
        [sc setImage:(int)indexPath.row];
        [window addSubview:sc];
        
    }
}

- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section
{
    return 1;
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{
    if (section == 1) {
        return 30;
    }else{
        return 1;
    }
}

- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section
{
    if (section == 1) {
        
        UIView * headerView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 30)];
        headerView.backgroundColor = [UIColor whiteColor];
        
        UILabel * honorLB = [[UILabel alloc] initWithFrame:CGRectMake(10, 5, 100, 20)];
        honorLB.text = @"所获荣誉/作品";
        honorLB.font = [UIFont systemFontOfSize:15.0];
        [headerView addSubview:honorLB];
        
        return headerView;
    }else{
        return nil;
    }
    
}


#pragma mark -----  getNewestStudentStar  获取商家最新的学员之星

- (void)getNewestStudentStar
{
    [SVProgressHUD showWithStatus:@""];
    
    
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    
    [params setObject:[gloabFunction getShopCode] forKey:@"shopCode"];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    NSString* vcode = [gloabFunction getSign:@"getNewestStudentStar" strParams:[gloabFunction getShopCode]];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    
    __weak typeof(self) weakSelf = self;
    [self.jsonPrcClient invokeMethod:@"getNewestStudentStar" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        
        if ([responseObject isKindOfClass:[NSDictionary class]]) {
            self.dataSource = [NSDictionary dictionaryWithDictionary:responseObject];
            [self setViewUp];
        }else{
            CSAlert(@"暂无学员之星");
        }
        
//        NSLog(@"%@", self.dataSource);
        
        
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        
        
        [SVProgressHUD dismiss];
        
        CSAlert(@"数据请求失败");
        
        
    }];

}







#pragma mark -----  getShopTeacherInfo
- (void)getShopTeacherInfo
{
    [SVProgressHUD showWithStatus:@""];
    
    
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    
    [params setObject:self.teacherCode forKey:@"teacherCode"];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    NSString* vcode = [gloabFunction getSign:@"getShopTeacherInfo" strParams:self.teacherCode];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    
    __weak typeof(self) weakSelf = self;
    [self.jsonPrcClient invokeMethod:@"getShopTeacherInfo" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        
        
        if ([responseObject isKindOfClass:[NSDictionary class]]) {
            self.dataSource = [NSDictionary dictionaryWithDictionary:responseObject];
            [self setViewUp];
        }else{
            CSAlert(@"数据出错");
        }
        
//        self.dataSource = [NSDictionary dictionaryWithDictionary:responseObject];
////        NSLog(@"%@", self.dataSource);
//        [self setViewUp];

        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        
        
        [SVProgressHUD dismiss];
        
        CSAlert(@"数据请求失败");
        
        
    }];

}



#pragma mark ------ 删除方法  新星
- (void)deleteClick:(UIButton *)sender
{
    if (self.number == 1) {
        [self delShopTeacher];
    }else if (self.number == 2){
        BMSQ_AddStudentViewController * addStudentVC = [[BMSQ_AddStudentViewController alloc] init];
        [self.navigationController pushViewController:addStudentVC animated:YES];
    }
    
    
}
#pragma mark ------ 修改方法  编辑
- (void)amendClick:(UIButton *)sender
{
    
    if (self.number == 1) {
        
        BMSQ_AddStarViewController * addStarVC = [[BMSQ_AddStarViewController alloc] init];
        addStarVC.teacherName = self.dataSource[@"teacherName"];
        addStarVC.teacherTitle = self.dataSource[@"teacherTitle"];
        addStarVC.teachCourse = self.dataSource[@"teachCourse"];
        addStarVC.teacherImgUrl = self.dataSource[@"teacherImgUrl"];
        addStarVC.teacherInfo = self.dataSource[@"teacherInfo"];
        addStarVC.teacherCode = self.dataSource[@"teacherCode"];
        addStarVC.teacherWork = self.dataSource[@"teacherWork"];
        [self.navigationController pushViewController:addStarVC animated:YES];
        
    }else if (self.number == 2){
        BMSQ_AddStudentViewController * addVC = [[BMSQ_AddStudentViewController alloc] init];
        addVC.studentName = self.dataSource[@"studentName"];
        addVC.starCode = self.dataSource[@"starCode"];
        addVC.starInfo = self.dataSource[@"starInfo"];
        addVC.starName = self.dataSource[@"starName"];
        addVC.starUrl = self.dataSource[@"starUrl"];
        addVC.starWork = self.dataSource[@"starWork"];
        [self.navigationController pushViewController:addVC animated:YES];
    }
    
    
}

#pragma mark ------ delShopTeacher 删除名师
- (void)delShopTeacher
{
    [SVProgressHUD showWithStatus:@""];
    
    
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    
    [params setObject:self.teacherCode forKey:@"teacherCode"];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    NSString* vcode = [gloabFunction getSign:@"delShopTeacher" strParams:self.teacherCode];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    
    __weak typeof(self) weakSelf = self;
    [self.jsonPrcClient invokeMethod:@"delShopTeacher" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        
        NSString * code = responseObject[@"code"];
        if (code.intValue == 50000) {
            CSAlert(@"删除成功");
            
            [[NSNotificationCenter defaultCenter] postNotificationName:@"StarTeacherList" object: nil];
            
            BMSQ_StarTeacherViewController * starVC = self.navigationController.viewControllers[1];
            [self.navigationController popToViewController:starVC animated:YES];
            
        }else if (code.intValue == 20000){
            CSAlert(@"删除失败");
        }else if (code.intValue == 77016){
            CSAlert(@"该老师有开课，不能删除");
        }
        
        
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        
        
        [SVProgressHUD dismiss];
        
        CSAlert(@"数据请求失败");
        
        
    }];

}

#pragma mark ------ 接收通知 
- (void)teacherDetailListNot:(NSNotification *)notification
{
    [self getNewestStudentStar];
}

- (void)dealloc
{
    
    // gatheringList
    NSNotificationCenter *gatheringList = [NSNotificationCenter defaultCenter];
    [gatheringList removeObserver:self
                             name:@"teacherDetailList"
                           object:nil];
    
}



#pragma mark ----- 内存警告
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


@end
