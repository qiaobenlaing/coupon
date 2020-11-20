//
//  BMSQ_TeaDetailViewController.m
//  BMSQC
//
//  Created by liuqin on 16/3/13.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "BMSQ_TeaDetailViewController.h"
#import "MobClick.h"
@interface BMSQ_TeaDetailViewController ()<UITableViewDataSource,UITableViewDelegate>

@property (nonatomic ,strong)UITableView *teacherTable;

@property (nonatomic ,strong)NSDictionary *teacherDic;
@property (nonatomic ,strong)NSArray *teacherWorkS;

@property (nonatomic ,strong)NSDictionary *stuDic;
@property (nonatomic ,strong)NSArray *starWorkS;

@end

@implementation BMSQ_TeaDetailViewController

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    [MobClick beginLogPageView:@"TeaDetailView"];// 
}
- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    [MobClick endLogPageView:@"TeaDetailView"];
}


- (void)viewDidLoad {
    [super viewDidLoad];
    [self setNavBackItem];
    [self setNavigationBar];
    [self setNavTitle:self.teaTitel];
    [self setNavBackGroundColor:[UIColor clearColor]];
    UIImageView *navImage = [[UIImageView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, APP_VIEW_ORIGIN_Y)];
    [navImage setImage:[UIImage imageNamed:@"app_nav"]];
    navImage.tag = 999;
    [self.navigationView addSubview:navImage];
    [self.navigationView  sendSubviewToBack:navImage];
    if (self.isTeacher) {
        [self getShopTeacherInfo];

    }else{
        [self getStudentStarInfo];
    }
    
    self.teacherTable = [[UITableView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, APP_VIEW_HEIGHT)];
    self.teacherTable.backgroundColor = [UIColor clearColor];
    self.teacherTable.delegate = self;
    self.teacherTable.dataSource = self;
    self.teacherTable.separatorStyle = UITableViewCellSeparatorStyleNone;
    [self.view insertSubview:self.teacherTable belowSubview:self.navigationView];

}

-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    if (section ==1) {
        if (self.isTeacher) {
            return self.teacherWorkS.count;
        }else{
            return self.starWorkS.count;
        }
    }else{
        return 3;
    }
}
-(NSInteger)numberOfSectionsInTableView:(UITableView *)tableView{
    return 2;
}
-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    if (indexPath.section ==1) {
        static NSString *identifi = @"teaDeatilPictureCell";
        
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifi];
        if (cell ==nil) {
            cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifi];
            cell.backgroundColor = [UIColor clearColor];
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            UIImageView *bgImageView =[[UIImageView alloc]initWithFrame:CGRectMake(10, 5, APP_VIEW_WIDTH-20,  APP_VIEW_HEIGHT/4-10)];
            [bgImageView setImage:[UIImage imageNamed:@"iv_noDataHome"]];
            bgImageView.tag = 100;
            [cell addSubview:bgImageView];
        }
        
        UIImageView *bgImageView = (UIImageView *)[cell viewWithTag:100];
        if (self.isTeacher) {
            NSDictionary *dic = [self.teacherWorkS objectAtIndex:indexPath.row];
            [bgImageView sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@",IMAGE_URL,[dic objectForKey:@"workUrl"]]] placeholderImage:[UIImage imageNamed:@"iv_noDataHome"]];
        }else{
            NSDictionary *dic = [self.starWorkS objectAtIndex:indexPath.row];
            [bgImageView sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@",IMAGE_URL,[dic objectForKey:@"starImgUrl"]]] placeholderImage:[UIImage imageNamed:@"iv_noDataHome"]];
        }
      
        return cell;
        
    }else{
        
        if (indexPath.row ==0) {
            static NSString *identifi = @"teaDeatilCell0";
            
            UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifi];
            if (cell ==nil) {
                cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifi];
                cell.selectionStyle = UITableViewCellSelectionStyleNone;
                UIImageView *bgImageView =[[UIImageView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, APP_VIEW_HEIGHT/2-50)];
                [bgImageView setImage:[UIImage imageNamed:@"iv_noDataHome"]];
                bgImageView.tag = 100;
                [cell addSubview:bgImageView];
            }
            
            UIImageView *bgImageView = (UIImageView *)[cell viewWithTag:100];
            if(self.isTeacher){
                [bgImageView sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@",IMAGE_URL,[self.teacherDic objectForKey:@"teacherImgUrl"]]] placeholderImage:[UIImage imageNamed:@"iv_noDataHome"]];
            }else{
                  [bgImageView sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@",IMAGE_URL,[self.stuDic objectForKey:@"starUrl"]]] placeholderImage:[UIImage imageNamed:@"iv_noDataHome"]];
            }
                
            
            
            return cell;
        }else if (indexPath.row ==1){
            
            
            static NSString *identifi = @"teaDeatilCell1";
            
            UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifi];
            if (cell ==nil) {
                cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifi];
                cell.selectionStyle = UITableViewCellSelectionStyleNone;
                
                cell.backgroundColor = [UIColor clearColor];
                UIView *bgView = [[UIView alloc]initWithFrame:CGRectMake(0, 8, APP_VIEW_WIDTH, 40)];
                bgView.backgroundColor = [UIColor whiteColor];
                [cell addSubview:bgView];
                float w = APP_VIEW_WIDTH/3;
                for (int i=0; i<3; i++) {
                    UILabel *myLabel = [[UILabel alloc]initWithFrame:CGRectMake(i*w, 0, w, 40)];
                    myLabel.font = [UIFont systemFontOfSize:14];
                    myLabel.textColor = APP_TEXTCOLOR;
                    myLabel.tag = 100*(i+1);
                    myLabel.textAlignment = NSTextAlignmentCenter;
                    [bgView addSubview:myLabel];
                }
            }
            
            UILabel *nameLabel = (UILabel *)[cell viewWithTag:100];
            UILabel *titleLabel = (UILabel *)[cell viewWithTag:200];
            UILabel *classLabel = (UILabel *)[cell viewWithTag:300];
            if (self.isTeacher) {
                nameLabel.text = [self.teacherDic objectForKey:@"teacherName"];
                titleLabel.text = [self.teacherDic objectForKey:@"teacherTitle"];
                classLabel.text = [self.teacherDic objectForKey:@"teachCourse"];

            }else{
                nameLabel.text = [self.stuDic objectForKey:@"starName"];
                titleLabel.text = [self.stuDic objectForKey:@"studentGrade"];
                classLabel.text =[NSString stringWithFormat:@"%@岁",[self.stuDic objectForKey:@"studentAge"]] ;
            }
            
          
            
            return cell;
            
        }else if (indexPath.row ==2){
            
            
            static NSString *identifi = @"teaDeatilCell1";
            
            UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifi];
            if (cell ==nil) {
                cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifi];
                cell.selectionStyle = UITableViewCellSelectionStyleNone;
                
                cell.textLabel.font = [UIFont systemFontOfSize:13];
                cell.textLabel.textColor = APP_TEXTCOLOR;
                cell.textLabel.numberOfLines = 0;
            }
            if (self.isTeacher) {
                  cell.textLabel.text = [NSString stringWithFormat:@"简介:%@",[self.teacherDic objectForKey:@"teacherInfo"]] ;
            }else{
               cell.textLabel.text = [NSString stringWithFormat:@"简介:%@",[self.stuDic objectForKey:@"starInfo"]] ;
            }
          

            return cell;
            
        }
    }
    
    static NSString *identifi = @"teaDeatilCell";
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifi];
    if (cell ==nil) {
        cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifi];
    }
    cell.textLabel.text = @"aaaa";
    return cell;
    
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    
    if (indexPath.section ==1) {
        return APP_VIEW_HEIGHT/4;
        
    }else{
        
        if (indexPath.row ==0) {
            return APP_VIEW_HEIGHT/2-50;
        }else if(indexPath.row ==1){
            return 56;
        }
        else if (indexPath.row ==2){
            if(self.isTeacher){
                
                NSString *str =[NSString stringWithFormat:@"简介:%@",[self.teacherDic objectForKey:@"teacherInfo"]] ;
                CGSize size = [str boundingRectWithSize:CGSizeMake(APP_VIEW_WIDTH, MAXFLOAT)
                                                options:NSStringDrawingUsesLineFragmentOrigin
                                             attributes:@{NSFontAttributeName:[UIFont systemFontOfSize:13.f]}
                                                context:nil].size;
                return size.height+20;
            }else{
                NSString *str =[NSString stringWithFormat:@"简介:%@",[self.teacherDic objectForKey:@"starInfo"]] ;
                CGSize size = [str boundingRectWithSize:CGSizeMake(APP_VIEW_WIDTH, MAXFLOAT)
                                                options:NSStringDrawingUsesLineFragmentOrigin
                                             attributes:@{NSFontAttributeName:[UIFont systemFontOfSize:13.f]}
                                                context:nil].size;
                return size.height+20;

            }
                
          
            
        }
        else{
            
            return 10;
        }
    }
}

-(UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section{
    if(section ==1){
        UILabel *label = [[UILabel alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 30)];
        label.textColor = APP_TEXTCOLOR;
        label.font = [UIFont systemFontOfSize:13.f];
        
        
        
        if (self.isTeacher) {
            label.text = @"   教师荣誉/作品";
        }else{
            label.text = @"   所获荣誉/作品/生活照";
            
        }
        
        return label;
    }else{
        return nil;
    }
}
-(CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{
    if (section ==1) {
        
        if (self.isTeacher) {
            if (self.teacherWorkS.count>0) {
                return 30;
                
            }else{
                return 0;
            }
        }else{
            if (self.starWorkS.count>0) {
                return 30;
                
            }else{
                return 0;
            }

        }
      
    }else{
        return 0;
    }
}

-(void)getShopTeacherInfo{
    
    [SVProgressHUD showWithStatus:@""];
    [self initJsonPrcClient:@"2"];
    
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:self.teacherCode forKey:@"teacherCode"];
    __weak typeof(self) weakSelf = self;
    [self.jsonPrcClient invokeMethod:@"getShopTeacherInfo" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        [SVProgressHUD dismiss];
        
        weakSelf.teacherDic = responseObject;
        weakSelf.teacherWorkS = [responseObject objectForKey:@"teacherWork"];
        [weakSelf.teacherTable reloadData];
    
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];
        
    }];

}
-(void)getStudentStarInfo{
    [SVProgressHUD showWithStatus:@""];
    [self initJsonPrcClient:@"2"];
    
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:self.starCode forKey:@"starCode"];
    __weak typeof(self) weakSelf = self;
    [self.jsonPrcClient invokeMethod:@"getStudentStarInfo" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        [SVProgressHUD dismiss];
        
        weakSelf.stuDic = responseObject;
        weakSelf.starWorkS = [responseObject objectForKey:@"starWork"];
        [weakSelf.teacherTable reloadData];
        
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];
        
    }];
}
-(void)scrollViewDidScroll:(UIScrollView *)scrollView{
    UIColor *color = APP_NAVCOLOR;
    CGFloat offset=scrollView.contentOffset.y;
    if (offset<=0) {
        UIImageView *navImage = [self.navigationView viewWithTag:999];
        navImage.hidden = NO;
        [self setNavBackGroundColor:[color colorWithAlphaComponent:0]];
    }else {
        CGFloat alpha=1-((200-offset)/200);
        UIImageView *navImage = [self.navigationView viewWithTag:999];
        navImage.hidden = YES;
        [self setNavBackGroundColor:[color colorWithAlphaComponent:alpha]];
    }
}

@end
